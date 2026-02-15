package owres.stockcomparer.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Declare root
        Parent root = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("/owres/stockcomparer/view/main-view.fxml")));

        // Create base scene
        Scene scene = new Scene(root);

        // Select css style
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/owres/stockcomparer/modern.css")).toExternalForm());

        // Setup Stage
        stage.setMinHeight(300);
        stage.setMinWidth(600);
        stage.setMaximized(true);
        stage.getIcons().add(new Image("file:src/main/resources/owres/stockcomparer/img/logo_circular.png"));
        stage.setTitle("Stock Comparer");
        stage.setScene(scene);
        stage.show();

        VBox graphContainer = (VBox) root.lookup("#graphContainer");

        FXMLLoader graphLoader = new FXMLLoader(
                getClass().getResource("/owres/stockcomparer/view/graph-view.fxml")
        );
        Pane graphComponent = graphLoader.load();

        graphContainer.getChildren().add(graphComponent);

    }
}
