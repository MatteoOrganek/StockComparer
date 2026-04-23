package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;
import owres.stockcomparer.model.graph.*;

import java.util.List;

/**
 * Controller handling graph-view calls
 */
public class GraphController implements IGraphController, StockObserver {

    IInteraction interaction;

    IGraphModel graph;

    @FXML
    private javafx.scene.control.DatePicker startDatePicker;

    @FXML
    private javafx.scene.control.DatePicker endDatePicker;

    @FXML
    LineChart<String, Number> lineChart;

    public CategoryAxis x;
    public NumberAxis y;
    public Stock currentStock;

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

        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateAndUpdate());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateAndUpdate());
    }
    public void drawGraph() {
        // Clear previous data
        lineChart.getData().clear();

        //Update graph to user selected stock instead of default
        if (currentStock != null) {
            //If graph is a Graph object
            if (graph instanceof GraphModel graphModel) {
                //calls upon user selected stock
                graphModel.setStock(currentStock);
            }

            //Asks graph class for stock data
            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                return;
            }

            var start = startDatePicker.getValue().atStartOfDay();
            var end = endDatePicker.getValue().atStartOfDay();

            PriceHistory history = graph.getData(currentStock, start, end);

            //Checks if we get a PriceHistory object, if it contains a list and if the list actually contains stock price entries
            if (history == null || history.getEntries() == null || history.getEntries().isEmpty()) {
                return;
            }

            //sends real price entries into updateYAxisBounds method
            updateYAxisBounds(history.getEntries());

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            //Sets graph label to real stock symbol (example TSLA , AAPL)
            series.setName(history.getStock().getSymbol());

            // Map PriceEntry objects to Chart Data
            for (PriceEntry entry : history.getEntries()) {
                // X = Date (String), Y = Close Price (Double)
                String dateLabel = entry.getTime().toLocalDate().toString();
                series.getData().add(new XYChart.Data<>(dateLabel, entry.getClosePrice()));
            }

            lineChart.getData().add(series);
        }
    }
    //Calculates graphs vertical scale using price entries
    //
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

    private void validateAndUpdate() {

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            return;
        }

        var start = startDatePicker.getValue();
        var end = endDatePicker.getValue();

        // Prevent inverted range
        if (end.isBefore(start)) {
            System.out.println("Invalid date range: end is before start");

            endDatePicker.setValue(start.plusDays(1));
            return;
        }

        // Enforce max range of 2 years
        if (start.plusYears(2).isBefore(end)) {
            System.out.println("Date range exceeds 2 years limit");

            endDatePicker.setValue(start.plusYears(2));
            return;
        }

        // Prevent future dates
        var today = java.time.LocalDate.now();
        if (end.isAfter(today)) {
            System.out.println("End date cannot be in the future");

            endDatePicker.setValue(today);
            return;
        }

        // Trigger graph update safely
        drawGraph();
    }
}
