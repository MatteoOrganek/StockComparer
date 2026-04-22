package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import owres.stockcomparer.model.data.Company;
import owres.stockcomparer.model.graph.IInteraction;
import owres.stockcomparer.model.graph.Interaction;

import java.io.IOException;
import java.util.List;

public class MainController  {

    @FXML
    private IGraphController graphController;

    IInteraction interaction;

    @FXML
    private ComboBox<String> dropdownStock;
    public ComboBox<String> dropdownProfile;
    @FXML private VBox graphContainer;

    // On initialization, update
    @FXML
    public void initialize() throws IOException {

        FXMLLoader graphLoader = new FXMLLoader(
                getClass().getResource("/owres/stockcomparer/view/graph-view.fxml")
        );
        VBox graphComponent = graphLoader.load();

        // 2. Get the controller from the loader
        graphController = graphLoader.getController();

        // 3. Add the view to your container
        graphContainer.getChildren().add(graphComponent);
        interaction = new Interaction(graphController);
        loadDropdownStockData();
        loadDropdownProfileData();
    }

    @FXML
    private void handleDropdownStock() {
        String selected = dropdownStock.getValue();
        interaction.setCurrentStock(selected, selected, new Company(selected));
        System.out.println(selected);
    }

    @FXML
    private void handleDropdownProfile() {
        String selected = dropdownProfile.getValue();
        System.out.println(selected);
    }

    private void loadDropdownStockData() {
        List<String> items = List.of("TSLA", "AAPL", "S&P500");
        dropdownStock.getItems().setAll(items);
    }

    private void loadDropdownProfileData() {
        List<String> items = List.of("Alice", "Matteo", "Luca");
        dropdownProfile.getItems().setAll(items);
    }
}

