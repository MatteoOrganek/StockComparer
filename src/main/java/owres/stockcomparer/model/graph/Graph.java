package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.data.*;
import owres.stockcomparer.model.data.Api.ApiBridge;
import owres.stockcomparer.model.data.database.Database;

import java.time.LocalDateTime;
import java.util.List;


/**
 * This Class handles the data translation to 1`
 */
public class Graph implements IGraph {

    // Thanks to the Laws of Substitutability, dataProvider can become a database or API instance
    IDataProvider dataProvider;

    IInteraction interaction;

    IIndicator indicator;

    IProfile profile;

    PriceHistory data;

    Stock stock = new Stock("TSLA", "Tesla", new Company("Tesla"));

    // Empty constructor
    public Graph() {}

    // Default constructor
    public Graph(IDataProvider dataProvider, IInteraction interaction, IIndicator indicator, IProfile profile, PriceHistory data, Stock stock) {
        this.dataProvider = dataProvider;
        this.interaction = interaction;
        this.indicator = indicator;
        this.profile = profile;
        this.data = data;
        this.stock = stock;
    }

    public PriceHistory getData() {

        // Get database class
        System.out.println("Using Database...");
        dataProvider = new Database();
        // Try to fetch data using the database
        System.out.println("Getting data from Database...");
        data = tryFetchData(dataProvider);

        // If the database does not have the correct data, dataProvider is switched to APIBridge
        if (data == null) {
            System.out.println("Switching to Api...");
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

        return data;
    }

    private PriceHistory tryFetchData(IDataProvider dataProvider) {
        if (dataProvider.isAvailable(stock)){
            // Get data if available
            return dataProvider.getData(stock, LocalDateTime.now(), LocalDateTime.now());
        } else {
            System.out.println("Data not found for " + dataProvider.getClass().getSimpleName());
            return null;
        }
    }
}
