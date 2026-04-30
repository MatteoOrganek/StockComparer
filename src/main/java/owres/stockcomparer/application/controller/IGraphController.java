package owres.stockcomparer.application.controller;

import owres.stockcomparer.model.stock.Stock;

import java.util.List;

public interface IGraphController {
    void updateGraph();

    List<Stock> searchStock(String stock);
    void selectStock(Stock stock);
    void setCompareStock(Stock stock);
    void clearCompare();
}
