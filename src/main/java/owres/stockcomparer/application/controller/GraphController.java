package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.Stock;
import owres.stockcomparer.model.data.database.MockDataSource;
import owres.stockcomparer.model.graph.*;

import java.util.List;

/**
 * Controller handling graph-view calls
 */
public class GraphController implements IGraphController, StockObserver {

    IInteraction interaction;
    public CategoryAxis x;
    public NumberAxis y;
    @FXML
    LineChart<String, Number> lineChart;

    // Graph instance
    IGraphModel graph;

    Stock currentStock;

    /**
     * Function called on initialization, where graph is instanced and Listeners for window resize are setup.
     */
    @FXML
    public void initialize() {

        // Instantiate Graph
        graph = new GraphModel();


        // Setup Chart Appearance
        lineChart.setCreateSymbols(false);

        interaction = new Interaction(this);
    }
    public void drawGraph() {
        // Clear previous data
        lineChart.getData().clear();

        if (currentStock != null) {
            var mockHistory = MockDataSource.getData(currentStock.getSymbol(), currentStock.getName(), currentStock.getCompany().getName(), 30);

            updateYAxisBounds(mockHistory.getEntries());

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(mockHistory.getStock().getSymbol());

            // Map PriceEntry objects to Chart Data
            for (PriceEntry entry : mockHistory.getEntries()) {
                // X = Date (String), Y = Close Price (Double)
                String dateLabel = entry.getTime().toLocalDate().toString();
                series.getData().add(new XYChart.Data<>(dateLabel, entry.getClosePrice()));
            }

            lineChart.getData().add(series);
        }
    }
    private void updateYAxisBounds(List<PriceEntry> entries) {
        if (entries == null || entries.isEmpty()) return;

        // Find min and max close prices
        double min = entries.stream().mapToDouble(PriceEntry::getClosePrice).min().orElse(0);
        double max = entries.stream().mapToDouble(PriceEntry::getClosePrice).max().orElse(100);

        double range = max - min;
        // Handle the case where price is flat (range is 0)
        double padding = (range == 0) ? min * 0.1 : range * 0.1;

        // Configure the Y Axis (injected via @FXML)
        y.setAutoRanging(false); // Crucial: tell FX not to override your settings
        y.setLowerBound(min - padding);
        y.setUpperBound(max + padding);

        // Optional: adjust the tick unit so the labels don't get crowded
        y.setTickUnit(padding);
    }

    @Override
    public void updateGraph() {
        drawGraph();
    }

    @Override
    public List<Stock> searchStock(String stock) {
        return List.of();
    }

    @Override
    public void selectStock(Stock stock) {

    }

    @Override
    public void onStockChanged(Stock newStock) {

        System.out.println("Stock changed to: " + newStock.getSymbol());
        currentStock = newStock;
        drawGraph();
    }
}

