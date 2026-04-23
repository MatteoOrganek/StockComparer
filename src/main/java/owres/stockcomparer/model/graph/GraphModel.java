package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.data.Api.ApiBridge;
import owres.stockcomparer.model.data.database.Database;
import owres.stockcomparer.model.graph.indicator.IIndicator;
import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.Company;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;


/**
 * This Class handles the data translation to 1`
 */
public class GraphModel implements IGraphModel {

    // Thanks to the Laws of Substitutability, dataProvider can become a database or API instance
    IDataProvider dataProvider;

    IInteraction interaction;

    IIndicator indicator;

    IProfile profile;

    PriceHistory data;

    Stock stock = new Stock("TSLA", "Tesla", new Company("Tesla"));

    // Empty constructor
    public GraphModel() {}

    // Default constructor
    public GraphModel(IDataProvider dataProvider, IInteraction interaction, IIndicator indicator, IProfile profile, PriceHistory data, Stock stock) {
        this.dataProvider = dataProvider;
        this.interaction = interaction;
        this.indicator = indicator;
        this.profile = profile;
        this.data = data;
        this.stock = stock;
    }

    public PriceHistory getData() {
        //gets stock data from 30 days earlier up to the current time
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(30);

        // Get database class
        System.out.println("Using Database...");
        dataProvider = new Database();
        // Try to fetch data using the database
        System.out.println("Getting data from Database...");
        //Fetch data from database cache
        data = tryFetchData(dataProvider, startTime, endTime);

        // If the database does not have the correct data, dataProvider is switched to APIBridge
        if (data == null) {
            System.out.println("Switching to Api...");
            dataProvider = new ApiBridge();
            System.out.println("Getting data from ApiBridge...");
            data = tryFetchData(dataProvider, startTime, endTime);
        }

        if (data == null) {
            System.out.println("No suitable data found.");
        } else {
            System.out.println("Suitable data found.");
        }
        // Translate the data to be read by GraphController

        return data;
    }

    //avoids repeating same logic twice for database and Api data check
    private PriceHistory tryFetchData(IDataProvider dataProvider, LocalDateTime startTime, LocalDateTime endTime) {
        //if the data provider is a database it checks the cache, if its an ApiBridge it checks with live API
        if (dataProvider.isAvailable(stock)){
            PriceHistory history = dataProvider.getData(stock, startTime, endTime);
            //Checks if pricehistory exists, entries list exists and not empty
            if (history != null && history.getEntries() != null && !history.getEntries().isEmpty()) {
                return history;
            }
        }

        //Debugging message saying the provider could not give valid data
        System.out.println("Data not found for " + dataProvider.getClass().getSimpleName());
        return null;
    }

    //Lets controller pass in the stock the user selected
    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

