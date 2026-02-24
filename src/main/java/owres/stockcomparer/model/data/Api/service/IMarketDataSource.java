package owres.stockcomparer.model.data.Api.service;

import owres.stockcomparer.model.data.Api.domain.StockSeries;
import java.time.LocalDate;

public interface IMarketDataSource {
    StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception;
}
