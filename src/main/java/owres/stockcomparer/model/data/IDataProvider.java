package owres.stockcomparer.model.data;

import owres.stockcomparer.model.data.Api.StockExchange;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;

public interface IDataProvider {

    PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime);
    Boolean isAvailable(Stock stock);
    StockExchange getExchangeForStock(Stock stock);
}
