package owres.stockcomparer.model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * This class manages the user's watchlist.
 * It stores stock symbols in memory while the app is running.
 * It handles adding, removing, and checking symbols.
 */
public class WatchlistService {

    // This list stores the stock symbols the user has added
    private final List<String> symbols = new ArrayList<>();

    /*
     * Returns all symbols in the watchlist.
     * We return an unmodifiable list so other classes
     * cannot accidentally change the internal data.
     */
    public List<String> getAll() {
        return Collections.unmodifiableList(symbols);
    }

    /*
     * Adds a stock symbol to the watchlist.
     * Converts it to uppercase and trims spaces.
     * Prevents duplicates and blank entries.
     */
    public void add(String symbol) {
        symbol = symbol.toUpperCase().trim();

        if (!symbol.isBlank() && !symbols.contains(symbol)) {
            symbols.add(symbol);
        }
    }

    /*
     * Removes a stock symbol from the watchlist.
     * Also normalises input before removing.
     */
    public void remove(String symbol) {
        symbols.remove(symbol.toUpperCase().trim());
    }

    /*
     * Checks if a symbol is already in the watchlist.
     */
    public boolean contains(String symbol) {
        return symbols.contains(symbol.toUpperCase().trim());
    }
}