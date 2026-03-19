package owres.stockcomparer.model.data.Api;

import java.time.LocalDate;

/**
 * AlphaVantageMarket Data Source
 */
public class StockExchange implements IMarketDataSource {


    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
