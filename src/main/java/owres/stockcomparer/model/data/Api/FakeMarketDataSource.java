package owres.stockcomparer.model.data.Api;

import java.time.LocalDate;

public class FakeMarketDataSource implements IMarketDataSource {

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}


