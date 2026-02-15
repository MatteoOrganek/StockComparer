package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class GraphController {

    @FXML
    public Pane canvas;


    @FXML
    public void initialize() {
        drawLine();
        canvas.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            drawLine();
        });

        canvas.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            drawLine();
        });
    }

    public void drawLine() {

        clearLines();

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        Rectangle rect = new Rectangle(0, 0, width, height);
        rect.setStroke(Color.BLACK);   // outline color
        rect.setFill(Color.TRANSPARENT); // no fill
        canvas.getChildren().add(rect);

        Line diag = new Line(0, 0, width, height);
        diag.setStartX(0);
        diag.setStartY(0);
        diag.setStroke(Color.BLACK);
        canvas.getChildren().add(diag);

    }

    public void clearLines() {
        canvas.getChildren().clear();
    }
}
