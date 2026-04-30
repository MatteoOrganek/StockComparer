package owres.stockcomparer.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import owres.stockcomparer.model.stock.Company;
import owres.stockcomparer.model.graph.IInteraction;
import owres.stockcomparer.model.graph.Interaction;
import owres.stockcomparer.model.stock.Stock;

import java.io.IOException;
import java.util.List;

public class MainController  {

    @FXML
    private IGraphController graphController;

    IInteraction interaction;

    @FXML private ComboBox<String> dropdownStock;
    @FXML public ComboBox<String> dropdownProfile;
    @FXML private VBox graphContainer;
    @FXML private TextField compareSymbolField;
    @FXML private Button clearCompareButton;

    // On initialization, update
    @FXML
    public void initialize() throws IOException {

        FXMLLoader graphLoader = new FXMLLoader(
                getClass().getResource("/owres/stockcomparer/view/graph-view.fxml")
        );
        VBox graphComponent = graphLoader.load();

        // Get the controller from the loader
        graphController = graphLoader.getController();

        // Add the view to the container
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
        List<String> items = List.of("TSLA", "AAPL", "GOOGL", "AMZN", "NVDA", "META");
        dropdownStock.getItems().setAll(items);
    }

    private void loadDropdownProfileData() {
        List<String> items = List.of("Alice", "Matteo", "Luca");
        dropdownProfile.getItems().setAll(items);
    }

    @FXML
    private void handleAddCompare() {
        if (graphController == null) return;

        String symbol = compareSymbolField.getText();
        if (symbol == null || symbol.isBlank()) {
            System.out.println("No compare symbol entered");
            return;
        }

        symbol = symbol.trim().toUpperCase();

        List<Stock> results = graphController.searchStock(symbol);
        if (results == null || results.isEmpty()) {
            System.out.println("No stock found for: " + symbol);
            return;
        }

        graphController.setCompareStock(results.getFirst());
        clearCompareButton.setDisable(false);
    }

    @FXML
    private void handleClearCompare() {
        if (graphController == null) return;

        graphController.clearCompare();
        compareSymbolField.clear();
        clearCompareButton.setDisable(true);
    }
}

