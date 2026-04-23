package owres.stockcomparer.model.data.Api;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeMarketDataSource implements IDataProvider {

    private final Random random = new Random();

    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {

        List<PriceEntry> entries = new ArrayList<>();

        double price = generateBasePrice(stock.getSymbol());

        LocalDateTime current = startTime;

        while (!current.isAfter(endTime)) {

            // Simulate daily movement
            double change = (random.nextDouble() - 0.5) * 2; // -1 to +1
            price += change;

            double open = price + random.nextDouble();
            double close = price + random.nextDouble();
            double high = Math.max(open, close) + random.nextDouble();
            double low = Math.min(open, close) - random.nextDouble();
            long volume = 1000 + random.nextInt(10000);

            entries.add(new PriceEntry(current, open, close, high, low, volume));

            current = current.plusDays(1);
        }

        return new PriceHistory(stock, entries);
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        // Mock is always available
        return true;
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return new StockExchange("Mock Exchange", "MOCK");
    }

    private double generateBasePrice(String symbol) {
        // Stable base price per stock symbol (deterministic)
        return Math.abs(symbol.hashCode() % 500) + 50;
    }
}