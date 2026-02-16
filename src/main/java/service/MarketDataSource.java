package service;

import domain.StockSeries;
import java.time.LocalDate;

public interface MarketDataSource {
    StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception;
}
