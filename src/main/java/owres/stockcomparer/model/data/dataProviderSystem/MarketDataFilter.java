package owres.stockcomparer.model.data.dataProviderSystem;

import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

/**
 * Filter 1 in the DataProviderSystem pipeline.
 *
 * Responsibility: Validate the incoming request before any data is fetched.
 * Checks that:
 *   1. The stock symbol is non-null and non-empty
 *   2. The start date is before the end date
 *   3. The date range does not exceed 730 days (the 2-year maximum from the coursework spec)
 *
 * If all checks pass, the request is forwarded to the next filter.
 * If any check fails, the request is rejected and no further processing occurs.
 *
 * From the lectures:
 *   "Filters are independent — a filter has no knowledge of upstream
 *    or downstream filters."
 */
public class MarketDataFilter implements IMessage {

    private static final Logger LOGGER   = Logger.getLogger(MarketDataFilter.class.getName());

    /** Coursework requirement: maximum date range is 2 years = 730 days */
    static final long MAX_RANGE_DAYS = 730;

    /** Next filter in the pipeline (required interface) */
    private final IMessage next;

    /**
     * Construct this filter, wiring it to the next filter via IMessage.
     *
     * @param next The next filter in the pipeline (DataFetcher)
     */
    public MarketDataFilter(IMessage next) {
        this.next = next;
    }

    /**
     * Validate the request and forward to the next filter if valid.
     */
    @Override
    public void transmit(PipelineRequest request) {

        // --- Guard: already rejected by an upstream filter ---
        if (request.isRejected()) return;

        // --- Validation 1: stock must not be null ---
        if (request.getStock() == null) {
            String reason = "Stock must not be null";
            LOGGER.warning("MarketDataFilter rejected request: " + reason);
            request.reject(reason);
            return;
        }

        // --- Validation 2: stock symbol must not be blank ---
        String symbol = request.getStock().getSymbol();
        if (symbol == null || symbol.isBlank()) {
            String reason = "Stock symbol must not be null or blank";
            LOGGER.warning("MarketDataFilter rejected request: " + reason);
            request.reject(reason);
            return;
        }

        // --- Validation 3: start must be before end ---
        if (request.getStartTime() == null || request.getEndTime() == null) {
            String reason = "Start time and end time must not be null";
            LOGGER.warning("MarketDataFilter rejected request: " + reason);
            request.reject(reason);
            return;
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            String reason = "Start date (" + request.getStartTime().toLocalDate()
                    + ") must not be after end date ("
                    + request.getEndTime().toLocalDate() + ")";
            LOGGER.warning("MarketDataFilter rejected request: " + reason);
            request.reject(reason);
            return;
        }

        // --- Validation 4: date range must not exceed 730 days ---
        long days = ChronoUnit.DAYS.between(request.getStartTime(), request.getEndTime());
        if (days > MAX_RANGE_DAYS) {
            String reason = "Date range of " + days + " days exceeds the maximum of "
                    + MAX_RANGE_DAYS + " days (2 years)";
            LOGGER.warning("MarketDataFilter rejected request: " + reason);
            request.reject(reason);
            return;
        }

        // --- All checks passed: forward to next filter ---
        LOGGER.info("MarketDataFilter: validated request for "
                + symbol + " (" + days + " days). Forwarding.");
        next.transmit(request);
    }
}