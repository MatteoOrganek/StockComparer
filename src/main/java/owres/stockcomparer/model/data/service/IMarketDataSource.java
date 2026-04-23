package owres.stockcomparer.model.data.service;

import java.time.LocalDate;

public interface IMarketDataSource {
    StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception;
}
