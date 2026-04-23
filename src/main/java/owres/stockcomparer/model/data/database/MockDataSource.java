package owres.stockcomparer.model.data.database;

import owres.stockcomparer.model.data.service.StockExchange;
import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.Company;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockDataSource implements IDataProvider {

    public static PriceHistory getData(String symbol, String stockName, String companyName, int days) {
        Random random = new Random();

        // 1. Create the Metadata
        Company company = new Company(companyName);
        Stock stock = new Stock(symbol, stockName, company);

        List<PriceEntry> entries = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);

        double currentPrice = 100.0 + random.nextDouble() * 50.0; // Start between 100 and 150

        // 2. Generate Daily Entries
        for (int i = 0; i < days; i++) {
            double volatility = 0.02; // 2% max movement per day
            double change = currentPrice * volatility * (random.nextDouble() * 2 - 1);

            double open = currentPrice;
            double close = open + change;
            double high = Math.max(open, close) + (random.nextDouble() * 2);
            double low = Math.min(open, close) - (random.nextDouble() * 2);
            long volume = 100000 + random.nextInt(900000);

            entries.add(new PriceEntry(
                    startTime.plusDays(i),
                    open,
                    close,
                    high,
                    low,
                    volume
            ));

            currentPrice = close; // Next day starts where today ended
        }

        return new PriceHistory(stock, entries);
    }

    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        return null;
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return null;
    }
}
