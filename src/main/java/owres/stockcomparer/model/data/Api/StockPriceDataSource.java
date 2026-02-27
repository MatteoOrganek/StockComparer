package owres.stockcomparer.model.data.Api;

import owres.stockcomparer.model.data.IStockData;

import java.time.LocalDate;

public class StockPriceDataSource implements IMarketDataSource {

    IStockData stockData;

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
