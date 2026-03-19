package owres.stockcomparer.model.data.Api;


import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.PriceEntry;
import owres.stockcomparer.model.data.Stock;

import java.time.LocalDateTime;

public class ApiBridge implements IDataProvider {

    IMarketDataSource marketDataSource;

    @Override
    public PriceEntry getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        return true;
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return null;
    }
}
