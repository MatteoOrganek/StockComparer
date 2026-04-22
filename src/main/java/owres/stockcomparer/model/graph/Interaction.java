package owres.stockcomparer.model.graph;

import owres.stockcomparer.application.controller.IGraphController;
import owres.stockcomparer.application.controller.StockObserver;
import owres.stockcomparer.model.data.Company;
import owres.stockcomparer.model.data.Stock;
import owres.stockcomparer.model.graph.IInteraction;

import java.util.ArrayList;
import java.util.List;

public class Interaction implements IInteraction {

    private final List<StockObserver> observers = new ArrayList<>();
    private Stock currentStock;
    private IGraphController iGraphController;

    public Interaction(IGraphController iGraphController) {
        this.iGraphController = iGraphController;
        // If your controller is an observer, add it automatically
        if (iGraphController instanceof StockObserver) {
            addObserver((StockObserver) iGraphController);
        }
    }

    // --- Observer Management ---
    public void addObserver(StockObserver observer) {
        System.out.println("Adding stock observer...");
        observers.add(observer);
    }

    private void notifyObservers() {
        for (StockObserver observer : observers) {
            System.out.println("Stock changed!");
            observer.onStockChanged(currentStock);
        }
    }

    // --- Modified Setter ---
    @Override
    public void setCurrentStock(String symbol, String name, Company company) {
        this.currentStock = new Stock(symbol, name, company);
        // Alert everyone that the data changed!
        notifyObservers();
    }

    @Override
    public Stock getCurrentStock() {
        return currentStock;
    }

    @Override
    public void changeIndicator(String newIndicator) {
    }
}