package owres.stockcomparer.model.data.Api;


import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;

public class ApiBridge implements IDataProvider {

    IMarketDataSource marketDataSource;

    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
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
