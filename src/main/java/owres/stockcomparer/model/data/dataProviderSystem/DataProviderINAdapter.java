package owres.stockcomparer.model.data.dataProviderSystem;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.database.Database;
import owres.stockcomparer.model.data.service.AlphaVantageService;
import owres.stockcomparer.model.data.service.StockExchange;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Entry adapter for the DataProviderSystem pipeline.
 *
 * Problem: External components call IDataProvider (getData, isAvailable,
 * getExchangeForStock), but the internal pipeline uses IMessage (transmit).
 * These interfaces are incompatible.
 *
 * Solution: This adapter implements IDataProvider externally and translates
 * each call into a PipelineRequest that is then transmitted through the
 * IMessage pipeline.
 *
 * From the lectures (RepairSys example):
 *   "We need an adapter at the entry end of the pipeline to convert
 *    messages sent via the external interface into messages that can be
 *    processed through the IMessage interface."
 *
 *   RepairChainINAdapter : provided=IRepairs, required=IMessage
 *   DataProviderINAdapter: provided=IDataProvider, required=IMessage
 */
public class DataProviderINAdapter implements IDataProvider {

    private static final Logger LOGGER = Logger.getLogger(DataProviderINAdapter.class.getName());

    /** First filter in the pipeline — the required IMessage interface */
    private final IMessage firstFilter;

    /**
     * Wire this adapter to the start of the filter chain.
     *
     * @param firstFilter The first IMessage filter to send requests to
     */
    public DataProviderINAdapter(IMessage firstFilter) {
        this.firstFilter = firstFilter;
    }

    /**
     * Convert an IDataProvider.getData() call into a PipelineRequest
     * and transmit it through the filter chain.
     *
     * Returns null if the request was rejected or no data was found.
     */
    @Override
    public PriceHistory getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        // Create the data carrier and send into the pipeline
        PipelineRequest request = new PipelineRequest(stock, startTime, endTime);
        firstFilter.transmit(request);

        if (request.isRejected()) {
            LOGGER.warning("Pipeline rejected request for "
                    + (stock != null ? stock.getSymbol() : "null")
                    + ": " + request.getRejectionReason());
            return null;
        }
        return request.getResult();
    }

    /**
     * Check availability via both offline and online sources.
     * Bypasses the pipeline — this is a lightweight probe, not a full fetch.
     */
    @Override
    public Boolean isAvailable(Stock stock) {
        // Check offline cache first (cheapest)
        Database db = new Database();
        if (db.isAvailable(stock)) return true;

        // Check online API as fallback
        AlphaVantageService api = new AlphaVantageService();
        return api.isAvailable(stock);
    }

    /**
     * Retrieve the stock exchange directly from the online API.
     * Exchange information is not part of the pipeline.
     */
    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        AlphaVantageService api = new AlphaVantageService();
        return api.getExchangeForStock(stock);
    }
}