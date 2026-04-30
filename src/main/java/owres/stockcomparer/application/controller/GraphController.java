package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import owres.stockcomparer.model.stock.Company;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;
import owres.stockcomparer.model.graph.*;

import java.util.ArrayList;
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
    public Stock compareStock;
    private boolean isComparing = false;

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

    // Set a stock to compare against and enable overlay mode
    public void setCompareStock(Stock stock) {
        this.compareStock = stock;
        this.isComparing = (stock != null);
        drawGraph(isComparing);
    }

    // Clear the comparison stock and return to single-stock view
    public void clearCompare() {
        this.compareStock = null;
        this.isComparing = false;
        drawGraph(false);
    }

    public void drawGraph(Boolean compare) {
        lineChart.getData().clear();

        if (currentStock == null) return;
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) return;

        var start = startDatePicker.getValue().atStartOfDay();
        var end   = endDatePicker.getValue().atStartOfDay();

        // Primary stock series
        PriceHistory primaryHistory = graph.getData(currentStock, start, end);

        if (primaryHistory == null
                || primaryHistory.getEntries() == null
                || primaryHistory.getEntries().isEmpty()) {
            return;
        }

        XYChart.Series<String, Number> primarySeries = new XYChart.Series<>();
        primarySeries.setName(primaryHistory.getStock().getSymbol());

        for (PriceEntry entry : primaryHistory.getEntries()) {
            String dateLabel = entry.getTime().toLocalDate().toString();
            primarySeries.getData().add(new XYChart.Data<>(dateLabel, entry.getClosePrice()));
        }

        lineChart.getData().add(primarySeries);

        // Comparison stock series (only when compare mode is on)
        if (compare && compareStock != null) {
            PriceHistory compareHistory = graph.getData(compareStock, start, end);

            if (compareHistory != null
                    && compareHistory.getEntries() != null
                    && !compareHistory.getEntries().isEmpty()) {

                XYChart.Series<String, Number> compareSeries = new XYChart.Series<>();
                compareSeries.setName(compareHistory.getStock().getSymbol());

                for (PriceEntry entry : compareHistory.getEntries()) {
                    String dateLabel = entry.getTime().toLocalDate().toString();
                    compareSeries.getData().add(new XYChart.Data<>(dateLabel, entry.getClosePrice()));
                }

                lineChart.getData().add(compareSeries);

                // Y-axis must cover BOTH stocks' price ranges
                updateYAxisBounds(primaryHistory.getEntries(), compareHistory.getEntries());
                return; // bounds already set, skip single-stock call below
            }
        }

        // Single stock — bounds from primary only
        updateYAxisBounds(primaryHistory.getEntries(), null);
    }

    //Calculates graphs vertical scale using price entries
    //
    private void updateYAxisBounds(List<PriceEntry> primary, List<PriceEntry> secondary) {
        if (primary == null || primary.isEmpty()) return;

        double min = primary.stream().mapToDouble(PriceEntry::getClosePrice).min().orElse(0);
        double max = primary.stream().mapToDouble(PriceEntry::getClosePrice).max().orElse(100);

        // Expand bounds to include the comparison stock if present
        if (secondary != null && !secondary.isEmpty()) {
            double secMin = secondary.stream().mapToDouble(PriceEntry::getClosePrice).min().orElse(min);
            double secMax = secondary.stream().mapToDouble(PriceEntry::getClosePrice).max().orElse(max);
            min = Math.min(min, secMin);
            max = Math.max(max, secMax);
        }

        double range   = max - min;
        double padding = (range == 0) ? min * 0.1 : range * 0.1;

        y.setAutoRanging(false);
        y.setLowerBound(min - padding);
        y.setUpperBound(max + padding);
        y.setTickUnit(padding);
    }

    @Override
    public void updateGraph() {
        drawGraph(isComparing); // ← pass current compare state, not always false
    }

    @Override
    public List<Stock> searchStock(String stock) {
        Stock foundStock = new Stock(stock, stock, new Company(stock));
        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(foundStock);
        return stocks;
    }

    @Override
    public void selectStock(Stock stock) {

    }

    @Override
    public void onStockChanged(Stock newStock) {

        System.out.println("Stock changed to: " + newStock.getSymbol());
        currentStock = newStock;
        drawGraph(isComparing);
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
        drawGraph(isComparing);
    }

}
