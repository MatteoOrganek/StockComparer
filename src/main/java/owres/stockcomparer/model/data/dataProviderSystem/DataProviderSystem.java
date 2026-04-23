package owres.stockcomparer.model.data.dataProviderSystem;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.service.AlphaVantageService;
import owres.stockcomparer.model.data.service.StockExchange;
import owres.stockcomparer.model.data.database.Database;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;


import java.time.LocalDateTime;

/**
 * <<Component>> DataProviderSystem
 *
 * Compound component that encapsulates the data-fetching pipeline.
 * External components see only the IDataProvider interface — they have no
 * knowledge of the internal filter chain or the offline/online strategy.
 *
 * Provided interface:  IDataProvider (getData, isAvailable, getExchangeForStock)
 * Required interfaces: Database (offline), YahooFinanceAPI (online) — injected
 */
public class DataProviderSystem implements IDataProvider {

    /** Entry adapter — translates IDataProvider calls into IMessage pipeline calls */
    private final DataProviderINAdapter inAdapter;

    /**
     * Default constructor. Assembles the pipeline with production components:

     * The pipeline is built right-to-left: each filter is wired to the next
     * before being passed into the previous one.
     */
    public DataProviderSystem() {
        // Concrete providers (injected into DataFetcher)
        Database       offlineDb = new Database();
        AlphaVantageService onlineApi = new AlphaVantageService();

        // Assemble pipeline from right to left
        // Filter 2 (terminal): fetch data offline-first, fallback online
        DataFetcher fetcher = new DataFetcher(offlineDb, onlineApi);

        // Filter 1: validate symbol and date range before fetching
        MarketDataFilter validator = new MarketDataFilter(fetcher);

        // Entry adapter: bridges IDataProvider API → IMessage pipeline
        this.inAdapter = new DataProviderINAdapter(validator);
    }

    /**
     * Testable constructor — allows injection of any IDataProvider implementations.
     * Useful for injecting mocks in unit tests without touching the file system or network.
     *
     * @param offlineProvider Local data source (e.g. Database or a test stub)
     * @param onlineProvider  Remote data source (e.g. YahooFinanceAPI or a test stub)
     */
    public DataProviderSystem(Database offlineProvider, IDataProvider onlineProvider) {
        DataFetcher      fetcher   = new DataFetcher(offlineProvider, onlineProvider);
        MarketDataFilter validator = new MarketDataFilter(fetcher);
        this.inAdapter            = new DataProviderINAdapter(validator);
    }

    /**
     * Fetch historical price data for the given stock and date range.
     *
     * Returns null if the request was rejected (e.g. date range > 2 years)
     * or if no data was found from either source.
     *
     * @param stock     The stock to fetch data for
     * @param startTime Start of the date range (inclusive)
     * @param endTime   End of the date range (inclusive, max 730 days from start)
     * @return PriceHistory with entries, or null if unavailable
     */
    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        return inAdapter.getData(stock, startTime, endTime); // <<delegate>>
    }

    /**
     * Check whether data for the given stock is available from any source.
     *
     * @param stock The stock to check
     * @return true if data is available offline or online
     */
    @Override
    public Boolean isAvailable(Stock stock) {
        return inAdapter.isAvailable(stock); // <<delegate>>
    }

    /**
     * Retrieve the stock exchange on which the given stock is listed.
     *
     * @param stock The stock to look up
     * @return StockExchange, or null if the exchange cannot be determined
     */
    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return inAdapter.getExchangeForStock(stock); // <<delegate>>
    }

}

