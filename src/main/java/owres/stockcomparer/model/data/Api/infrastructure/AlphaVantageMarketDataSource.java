package owres.stockcomparer.model.data.Api.infrastructure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import owres.stockcomparer.model.data.Api.domain.PricePoint;
import owres.stockcomparer.model.data.Api.domain.StockSeries;
import owres.stockcomparer.model.data.Api.service.IMarketDataSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AlphaVantageMarketDataSource implements IMarketDataSource {

    private final String apiKey;
    private final HttpClient http = HttpClient.newHttpClient();

    public AlphaVantageMarketDataSource(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        // Alpha Vantage daily time series docs use function=TIME_SERIES_DAILY (or ADJUSTED).
        // We'll request outputsize=full so we can filter the date range ourselves.
        String url = "https://www.alphavantage.co/query"
                + "?function=TIME_SERIES_DAILY"
                + "&symbol=" + symbol
                + "&outputsize=compact"
                + "&apikey=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP error: " + response.statusCode());
        }

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

        if (root.has("Error Message")) {
            throw new RuntimeException("API error: " + root.get("Error Message").getAsString());
        }

        if (root.has("Note")) {
            throw new RuntimeException("API limit message: " + root.get("Note").getAsString());
        }

        if (root.has("Information")) {
            throw new RuntimeException("API info: " + root.get("Information").getAsString());
        }

        JsonObject seriesObj = root.getAsJsonObject("Time Series (Daily)");
        if (seriesObj == null) {
            String body = response.body();
            String preview = body.length() > 400 ? body.substring(0, 400) + "..." : body;
            throw new RuntimeException("Unexpected response (no Time Series). Body preview: " + preview);
        }

        List<PricePoint> points = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : seriesObj.entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey()); // format YYYY-MM-DD

            if (date.isBefore(from) || date.isAfter(to)) continue;

            JsonObject day = entry.getValue().getAsJsonObject();
            double close = day.get("4. close").getAsDouble();

            points.add(new PricePoint(date, close));
        }

        // Sort ascending by date for charting
        points.sort(Comparator.comparing(PricePoint::getDate));

        return new StockSeries(symbol.toUpperCase(), points);
    }
}
