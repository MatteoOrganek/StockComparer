package owres.stockcomparer.model.data.Api.service;

import owres.stockcomparer.model.data.Api.domain.StockSeries;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StockPriceService {

    private final IMarketDataSource dataSource;

    public StockPriceService(IMarketDataSource dataSource) {
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
