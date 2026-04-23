package owres.stockcomparer.model.data.service;


import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;
import owres.stockcomparer.model.data.database.Database;

import java.time.LocalDateTime;



//This is an SOA broker
public class ApiBridge implements IDataProvider {
    //Creates a field to store real Api provider
    private final IDataProvider liveApi;
    //Creates field for database to save stock data on cache
    private final Database database;

    //Constructer setting up what the bridge needs, online source and local storage
    public ApiBridge() {
        //liveApi is what goes online and gets stock data, object is created when bridge starts
        this.liveApi = new AlphaVantageService();
        //Will be used to save data into JSON cache files
        this.database = new Database();
    }

    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        //Gets stock data from live Api using stock name, start and end time giving back a PriceHistory that contains a list of PriceEntry objects
        PriceHistory history = liveApi.getData(stock, startTime, endTime);

        //Checks that Api returned an object, if the list of price entries exist, and if the price entry list is not empty
        if (history != null && history.getEntries() != null && !history.getEntries().isEmpty()) {
           //Saves fetched online data in local storage
            database.saveEntries(stock, history.getEntries());
            //Returns live data
            return history;
        }
        return null;
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        //Checks if stock is available
        return liveApi.isAvailable(stock);
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        //Gets infor about stock exchange (job is given to Api class, same as isAvailable method)
        return liveApi.getExchangeForStock(stock);
    }
}
