package owres.stockcomparer.model.stock;

import java.util.List;

/**
 * This class defines the history of price entries for a specific stock.
 */
public class PriceHistory {

    Stock stock;
    List<PriceEntry> entries;

    /**
     * Basic Constructor
     * @param stock Stock
     * @param entries Entries
     */
    public PriceHistory(Stock stock, List<PriceEntry> entries) {
        this.stock = stock;
        this.entries = entries;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<PriceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PriceEntry> entries) {
        this.entries = entries;
    }
}
