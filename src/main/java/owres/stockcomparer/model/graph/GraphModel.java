package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.data.dataProviderSystem.DataProviderSystem;
import owres.stockcomparer.model.graph.Profile.IProfile;
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

    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {

        DataProviderSystem dataProviderSystem = new DataProviderSystem();
        return dataProviderSystem.getData(stock, startTime, endTime);
    }

    //Lets controller pass in the stock the user selected
    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

