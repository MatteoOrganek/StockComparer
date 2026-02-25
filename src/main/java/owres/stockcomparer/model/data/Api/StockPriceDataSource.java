package owres.stockcomparer.model.data.Api;

import java.time.LocalDate;

public class StockPriceDataSource implements IMarketDataSource {

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
