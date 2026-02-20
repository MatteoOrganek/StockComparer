package owres.stockcomparer.model.Api.infrastructure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import owres.stockcomparer.model.Api.domain.PricePoint;
import owres.stockcomparer.model.Api.domain.StockSeries;
import owres.stockcomparer.model.Api.service.MarketDataSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/*
 * This class connects to the Alpha Vantage API.
 * It sends an HTTP request, receives JSON data,
 * and converts it into our internal StockSeries format.
 */
public class AlphaVantageMarketDataSource implements MarketDataSource {

    // API key used to authenticate requests
    private final String apiKey;
    // Java HTTP client used to send requests

    private final HttpClient http = HttpClient.newHttpClient();

    public AlphaVantageMarketDataSource(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        // Build the API request URL
        String url = "https://www.alphavantage.co/query"
                + "?function=TIME_SERIES_DAILY"
                + "&symbol=" + symbol
                + "&outputsize=compact"
                + "&apikey=" + apiKey;
        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send request and receive response
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        // Basic HTTP status check
        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP error: " + response.statusCode());
        }

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

        // Handle common API error responses
        if (root.has("Error Message")) {
            throw new RuntimeException("API error: " + root.get("Error Message").getAsString());
        }

        if (root.has("Note")) {
            throw new RuntimeException("API limit message: " + root.get("Note").getAsString());
        }

        if (root.has("Information")) {
            throw new RuntimeException("API info: " + root.get("Information").getAsString());
        }

        // Extract the actual time series data
        JsonObject seriesObj = root.getAsJsonObject("Time Series (Daily)");
        if (seriesObj == null) {
            String body = response.body();
            String preview = body.length() > 400 ? body.substring(0, 400) + "..." : body;
            throw new RuntimeException("Unexpected response (no Time Series). Body preview: " + preview);
        }

        List<PricePoint> points = new ArrayList<>();

        // Loop through each date entry in the JSON
        for (Map.Entry<String, JsonElement> entry : seriesObj.entrySet()) {
            // Loop through each date entry in the JSON
            LocalDate date = LocalDate.parse(entry.getKey()); // format YYYY-MM-DD

            // Filter to only requested date range
            if (date.isBefore(from) || date.isAfter(to)) continue;

            JsonObject day = entry.getValue().getAsJsonObject();

            // Extract closing price
            double close = day.get("4. close").getAsDouble();

            points.add(new PricePoint(date, close));
        }

        //Sort ascending by date
        points.sort(Comparator.comparing(PricePoint::getDate));

        return new StockSeries(symbol.toUpperCase(), points);
    }
}
