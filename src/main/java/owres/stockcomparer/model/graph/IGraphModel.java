package owres.stockcomparer.model.graph;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;

public interface IGraphModel {
    PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime);
}
