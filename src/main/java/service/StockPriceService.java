package service;

import domain.StockSeries;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StockPriceService {

    private final MarketDataSource dataSource;

    public StockPriceService(MarketDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StockSeries getPrices(String symbol, LocalDate from, LocalDate to) throws Exception {

        long days = ChronoUnit.DAYS.between(from, to);
        if (days > 730) {  // 2 year limit
            throw new IllegalArgumentException("Date range exceeds 2 years.");
        }

        return dataSource.fetchDailyCloses(symbol, from, to);
    }
}
