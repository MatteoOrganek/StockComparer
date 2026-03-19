package owres.stockcomparer.model.graph;



import owres.stockcomparer.model.data.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Watchlist implements IWatchList{

    private final List<Stock> symbols = new ArrayList<Stock>();

    public List<Stock> getAll() {
        return Collections.unmodifiableList(symbols);
    }

    public void add(Stock symbol) {
        symbols.add(symbol);
    }

    public void remove(String symbol) {
        symbols.remove(symbol.toUpperCase().trim());
    }

    public boolean contains(String symbol) {
        return symbols.contains(symbol.toUpperCase().trim());
    }
}

