package owres.stockcomparer.model.data;

import owres.stockcomparer.model.data.Api.StockExchange;

import java.time.LocalDateTime;

public interface IDataProvider {

    PriceEntry getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime);
    Boolean isAvailable(Stock stock);
    StockExchange getExchangeForStock(Stock stock);
}
