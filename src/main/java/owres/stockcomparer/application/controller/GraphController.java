package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import owres.stockcomparer.model.IGraph;
import owres.stockcomparer.model.graph.Graph;

public class GraphController {

    Graph graph;

    @FXML
    public Pane canvas;


    /**
     * Function called on initialization, where graph is instanced and Listeners for window resize are setup.
     */
    @FXML
    public void initialize() {

        // Instantiate Graph
        graph = new Graph();

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

        // Fetch JSON data
        String JSONData = graph.getJSON();

        // Based on the JSON data the program will create a graph out of it, translating prices into points in a canvas

        // Remove any lines in canvas
        clearLines();

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

    /**
     * This function deletes recursively all children in the canvas
     */
    public void clearLines() {
        canvas.getChildren().clear();
    }
}
