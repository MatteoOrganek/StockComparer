package owres.stockcomparer.application.controller;

import owres.stockcomparer.model.stock.Stock;

public interface StockObserver {
    void onStockChanged(Stock newStock);

}
