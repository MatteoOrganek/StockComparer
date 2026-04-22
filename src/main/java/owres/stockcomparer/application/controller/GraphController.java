package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import owres.stockcomparer.model.data.PriceEntry;
import owres.stockcomparer.model.data.Stock;
import owres.stockcomparer.model.graph.Graph;
import owres.stockcomparer.model.graph.IGraph;
import owres.stockcomparer.model.graph.Profile;

import java.util.List;
import java.util.Random;

/**
 * Controller handling graph-view calls
 */
public class GraphController implements IGraphController {


    @FXML
    private LineChart<Number, Number> lineChart;

    // Graph instance
    IGraph graph;

    /**
     * Function called on initialization, where graph is instanced and Listeners for window resize are setup.
     */
    @FXML
    public void initialize() {

        // Instantiate Graph
        graph = new Graph();

        // Fetch JSON data
        List<PriceEntry> data = graph.getData();

        // Draw graph
        drawGraph();

    }

    /**
     * This function is able to draw lines on a canvas based on the JSON input from graph
     */
    public LineChart<Number, Number> drawGraph() {

        // Based on the JSON data the program will create a graph out of it, translating prices into points in a canvas

        // Axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");

        // Chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false); // smoother look (no dots)

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Stock Name");

        // Generate random walk data
        Random random = new Random();
        double price = 100.0;

        for (int i = 0; i < 100; i++) {
            price += (random.nextDouble() - 0.5) * 2; // small fluctuations
            series.getData().add(new XYChart.Data<>(i, price));
        }

        lineChart.getData().add(series);

        return lineChart;

    }

    public void drawCandlesticks() {

        // Fetch data
        List<PriceEntry> data = graph.getData();

        // To be implemented in the next few sprints
    }

    /**
     * This function deletes recursively all children in the canvas
     */
    public void clearCanvas() {

    }

    @Override
    public void updateGraph(IGraph graph) {

        // Fetch data
        List<PriceEntry> data = graph.getData();
        drawGraph();
    }

    @Override
    public List<Stock> searchStock(String stock) {
        return List.of();
    }

    @Override
    public void selectStock(Stock stock) {

    }
}

