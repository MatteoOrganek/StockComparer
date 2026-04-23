package owres.stockcomparer.model.data.service;


import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlphaVantageService implements IDataProvider {

    private static final Logger log = LoggerFactory.getLogger(AlphaVantageService.class);

    private static final String API_KEY =
            System.getenv("ALPHAVANTAGE_API_KEY");
    private static final String BASE_URL =
            "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

    @Override
    public PriceHistory getData(Stock stock, LocalDateTime start, LocalDateTime end) {

        List<PriceEntry> entries = new ArrayList<>();

        try {
            String urlStr = BASE_URL + stock.getSymbol() + "&apikey=" + API_KEY;

            log.info("Requesting AlphaVantage data for {}", stock.getSymbol());

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            log.info("HTTP response code: {}", responseCode);

            if (responseCode != 200) {
                log.warn("Failed API response for {}", stock.getSymbol());
                return new PriceHistory(stock, entries);
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

            JsonObject timeSeries = json.getAsJsonObject("Time Series (Daily)");

            if (timeSeries == null) {
                log.warn("No time series data for {}", stock.getSymbol());
                return new PriceHistory(stock, entries);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (String dateStr : timeSeries.keySet()) {

                LocalDateTime date = LocalDateTime.parse(dateStr + "T00:00:00");

                if (date.isBefore(start) || date.isAfter(end)) continue;

                JsonObject day = timeSeries.getAsJsonObject(dateStr);

                double open = day.get("1. open").getAsDouble();
                double high = day.get("2. high").getAsDouble();
                double low = day.get("3. low").getAsDouble();
                double close = day.get("4. close").getAsDouble();
                long volume = day.get("5. volume").getAsLong();

                entries.add(new PriceEntry(date, open, close, high, low, volume));
            }

            log.info("Fetched {} entries for {}", entries.size(), stock.getSymbol());

        } catch (Exception e) {
            log.error("AlphaVantage API failure for {}", stock.getSymbol(), e);
        }

        return new PriceHistory(stock, entries);
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        log.info("Checking availability for {}", stock.getSymbol());
        return true;
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return new StockExchange("Unknown (AlphaVantage)", "N/A");
    }
}