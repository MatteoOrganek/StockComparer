package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.data.Api.infrastructure.ApiBridge;
import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.database.Database;


/**
 * This Class handles the data translation to 1`
 */
public class Graph implements IGraph {

    // Thanks to the Laws of Substitutability, dataProvider can become a database or API instance
    IDataProvider dataProvider;

    IInteraction interaction;

    IIndicator indicator;

    String data;

    // Default constructor
    public Graph() {}

    // Constructor with defined stock
    public Graph(String stockName) {}

    // TODO Output to be changed to JSON
    public String getData() {

        // Get database class
        System.out.println("Using Database...");
        dataProvider = new Database();
        // Try to fetch data using the database
        System.out.println("Getting data from Database...");
        data = tryFetchData(dataProvider);

        // If the database does not have the correct data, dataProvider is switched to APIBridge
        if (data == null) {
            System.out.println("Switching to ApiBridge...");
            dataProvider = new ApiBridge();
            System.out.println("Getting data from ApiBridge...");
            data = tryFetchData(dataProvider);
        }

        if (data == null) {
            System.out.println("No suitable data found.");
        } else {
            System.out.println("Suitable data found.");
        }
        // Translate the data to be read by GraphController
        String translatedData = data;

        return translatedData;
    }

    private String tryFetchData(IDataProvider dataProvider) {
        if (dataProvider.isAvailable()){
            // Get data if available
            return dataProvider.getData();
        } else {
            System.out.println("Data not found for " + dataProvider.getClass().getSimpleName());
            return null;
        }
    }
}
