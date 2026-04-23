package owres.stockcomparer.model.data.dataProviderSystem;

import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import java.time.LocalDateTime;

/**
 * Data carrier passed through each filter in the DataProviderSystem pipeline.
 *
 * Each filter reads from and writes to this object.
 * If a filter rejects the request, it calls reject() and downstream
 * filters check isRejected() before doing any work.
 *
 * This follows the lecture pattern where all filters share a common
 * data format passed via the IMessage interface.
 */
public class PipelineRequest {

    // --- Input (set by the entry adapter, never changed by filters) ---
    private final Stock         stock;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    // --- Output (set by DataFetcher if successful) ---
    private PriceHistory result;

    // --- Control (set by any filter that rejects the request) ---
    private boolean rejected;
    private String  rejectionReason;

    /**
     * Create a new pipeline request.
     *
     * @param stock     The stock to fetch data for
     * @param startTime Start of the date range (inclusive)
     * @param endTime   End of the date range (inclusive)
     */
    public PipelineRequest(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        this.stock     = stock;
        this.startTime = startTime;
        this.endTime   = endTime;
        this.rejected  = false;
    }

    // --- Called by filters that detect an error ---

    /**
     * Mark this request as rejected.
     * Downstream filters must check isRejected() before processing.
     *
     * @param reason Human-readable explanation logged for debugging
     */
    public void reject(String reason) {
        this.rejected        = true;
        this.rejectionReason = reason;
    }

    // --- Getters ---

    public Stock         getStock()           { return stock; }
    public LocalDateTime getStartTime()       { return startTime; }
    public LocalDateTime getEndTime()         { return endTime; }

    public PriceHistory  getResult()          { return result; }
    public void          setResult(PriceHistory result) { this.result = result; }

    public boolean       isRejected()         { return rejected; }
    public String        getRejectionReason() { return rejectionReason; }
}