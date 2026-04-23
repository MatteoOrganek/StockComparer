package owres.stockcomparer.model.data.database;

import owres.stockcomparer.model.stock.PriceEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one stock's cached price data stored as JSON.
 * File: data/prices/TSLA.json
 */
public class PriceHistoryRecord {

    private String symbol;
    private LocalDateTime lastFetched;
    private List<PriceEntry> entries;

    // Required by Jackson
    public PriceHistoryRecord() {
        this.entries = new ArrayList<>();
    }

    public PriceHistoryRecord(String symbol, LocalDateTime lastFetched, List<PriceEntry> entries) {
        this.symbol = symbol;
        this.lastFetched = lastFetched;
        this.entries = entries != null ? entries : new ArrayList<>();
    }

    public String getSymbol()                        { return symbol; }
    public void setSymbol(String symbol)             { this.symbol = symbol; }

    public LocalDateTime getLastFetched()            { return lastFetched; }
    public void setLastFetched(LocalDateTime t)      { this.lastFetched = t; }

    public List<PriceEntry> getEntries()             { return entries; }
    public void setEntries(List<PriceEntry> entries) { this.entries = entries; }
}