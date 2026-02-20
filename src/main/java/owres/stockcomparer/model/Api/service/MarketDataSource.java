package owres.stockcomparer.model.Api.service;

import owres.stockcomparer.model.Api.domain.StockSeries;
import java.time.LocalDate;

/*
 * This interface defines how stock data should be fetched.
 * It allows us to switch data providers without changing the rest of the app.
 */
public interface MarketDataSource {

    /*
     * Fetches daily closing prices for a stock
     * between two dates.
     */
    StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception;
}