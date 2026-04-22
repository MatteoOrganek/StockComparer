package owres.stockcomparer.application.controller;

import owres.stockcomparer.model.data.Stock;

public interface StockObserver {
    void onStockChanged(Stock newStock);

}
