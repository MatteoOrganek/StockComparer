package owres.stockcomparer.model.Api.service;

import owres.stockcomparer.model.Api.domain.StockSeries;
import java.time.LocalDate;

public interface MarketDataSource {
    StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception;
}
