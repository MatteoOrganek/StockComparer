package owres.stockcomparer.model.Api.service;

import owres.stockcomparer.model.Api.domain.StockSeries;

import java.time.LocalDate;

/*
 * This class acts as a middle layer between the controller
 * and the data source (API).
 * It applies business rules before fetching stock data.
 */
public class StockPriceService {

    private final MarketDataSource dataSource;

    public StockPriceService(MarketDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Fetches stock prices while enforcing
     * application rules (like max 2-year range).
     */
    public StockSeries getPrices(String symbol, LocalDate from, LocalDate to) throws Exception {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol cannot be empty.");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        // Business rule: limit to 2 years
        if (from.isBefore(to.minusYears(2))) {
            throw new IllegalArgumentException("Date range cannot exceed 2 years.");
        }

        return dataSource.fetchDailyCloses(symbol, from, to);
    }
}