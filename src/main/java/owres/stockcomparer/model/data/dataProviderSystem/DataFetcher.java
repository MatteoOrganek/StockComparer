package owres.stockcomparer.model.data.dataProviderSystem;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.database.Database;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.util.logging.Logger;

/**
 * Filter 2 (terminal) in the DataProviderSystem pipeline.
 *
 * Responsibility: Fetch price data using an offline-first strategy:
 *   1. Try the local JSON Database (offline cache) first
 *   2. If not available, fall back to the online provider (YahooFinanceAPI)
 *   3. On a successful online fetch, save the result to the Database for
 *      future offline use
 *
 * This is the last filter — it writes the result into the PipelineRequest
 * rather than forwarding to another filter.
 *
 * The two IDataProvider dependencies are injected via the constructor,
 * keeping this filter independent of concrete provider implementations
 * (Dependency Inversion Principle).
 */
public class DataFetcher implements IMessage {

    private static final Logger LOGGER = Logger.getLogger(DataFetcher.class.getName());

    /** Local JSON cache (offline) */
    private final Database      offlineProvider;

    /** Remote API (online fallback) */
    private final IDataProvider onlineProvider;

    /**
     * Construct this filter with the two data sources to try.
     *
     * @param offlineProvider Local JSON database (Database)
     * @param onlineProvider  Remote market data API (YahooFinanceAPI)
     */
    public DataFetcher(Database offlineProvider, IDataProvider onlineProvider) {
        this.offlineProvider = offlineProvider;
        this.onlineProvider  = onlineProvider;
    }

    /**
     * Attempt to fetch price data offline, falling back to online if needed.
     * Writes the result into request.setResult() on success.
     */
    @Override
    public void transmit(PipelineRequest request) {

        // --- Guard: already rejected upstream ---
        if (request.isRejected()) return;

        Stock stock     = request.getStock();
        String symbol   = stock.getSymbol();

        // --- Strategy 1: offline (database) ---
        LOGGER.info("DataFetcher: trying offline cache for " + symbol);
        if (offlineProvider.isAvailable(stock)) {
            PriceHistory offlineResult = offlineProvider.getData(
                    stock, request.getStartTime(), request.getEndTime()
            );
            if (offlineResult != null && !offlineResult.getEntries().isEmpty()) {
                LOGGER.info("DataFetcher: offline hit for " + symbol
                        + " (" + offlineResult.getEntries().size() + " entries)");
                request.setResult(offlineResult);
                return;
            }
        }

        // --- Strategy 2: online (API) ---
        LOGGER.info("DataFetcher: offline miss — trying online provider for " + symbol);
        if (onlineProvider.isAvailable(stock)) {
            PriceHistory onlineResult = onlineProvider.getData(
                    stock, request.getStartTime(), request.getEndTime()
            );
            if (onlineResult != null && !onlineResult.getEntries().isEmpty()) {
                LOGGER.info("DataFetcher: online hit for " + symbol
                        + " (" + onlineResult.getEntries().size() + " entries). Caching.");
                // Cache for future offline use
                offlineProvider.saveEntries(stock, onlineResult.getEntries());
                request.setResult(onlineResult);
                return;
            }
        }

        // --- Both sources failed ---
        String reason = "No data found for '" + symbol
                + "' from either offline cache or online provider";
        LOGGER.warning("DataFetcher: " + reason);
        request.reject(reason);
    }
}