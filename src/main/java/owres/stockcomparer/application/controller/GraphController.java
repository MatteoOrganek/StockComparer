package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import owres.stockcomparer.model.graph.Graph;
import owres.stockcomparer.model.graph.IGraph;

/**
 * Controller handling graph-view calls
 */
public class GraphController implements IGraphController {

    // Graph instance
    IGraph graph;

    @FXML
    public Pane canvas;


    /**
     * Function called on initialization, where graph is instanced and Listeners for window resize are setup.
     */
    @FXML
    public void initialize() {

        // Instantiate Graph
        graph = new Graph();

        // Fetch JSON data
        String data = graph.getData();

        // Draw default lines
        drawLine();

        // Crate Listener for width change
        canvas.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            drawLine();
        });

        // Crate Listener for height change
        canvas.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            drawLine();
        });
    }

    /**
     * This function is able to draw lines on a canvas based on the JSON input from graph
     */
    public void drawLine() {

        // Based on the JSON data the program will create a graph out of it, translating prices into points in a canvas

        // Remove any children in canvas
        clearCanvas();

        // Mock up a base shape to check for resize functionality
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        Rectangle rect = new Rectangle(0, 0, width, height);
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.TRANSPARENT);
        canvas.getChildren().add(rect);

        Line diag = new Line(0, 0, width, height);
        diag.setStartX(0);
        diag.setStartY(0);
        diag.setStroke(Color.BLACK);
        canvas.getChildren().add(diag);

    }

    public void drawCandlesticks() {

        // Fetch JSON data
        String JSONData = graph.getData();

        // To be implemented in the next few sprints
    }

    /**
     * This function deletes recursively all children in the canvas
     */
    public void clearCanvas() {
        canvas.getChildren().clear();
    }

    @Override
    public void renderGraph(IGraph graph) {

        // Fetch JSON data
        String data = graph.getData();
        drawLine();
    }
}

