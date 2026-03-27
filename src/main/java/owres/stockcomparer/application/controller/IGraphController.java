package owres.stockcomparer.application.controller;

import owres.stockcomparer.model.data.Stock;
import owres.stockcomparer.model.graph.IGraph;

import java.util.List;

public interface IGraphController {
    void updateGraph(IGraph graph);

    List<Stock> searchStock(String stock);
    void selectStock(Stock stock);
}
