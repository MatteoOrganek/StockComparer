package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WatchlistService {

    private final List<String> symbols = new ArrayList<>();

    public List<String> getAll() {
        return Collections.unmodifiableList(symbols);
    }

    public void add(String symbol) {
        symbol = symbol.toUpperCase().trim();
        if (!symbol.isBlank() && !symbols.contains(symbol)) {
            symbols.add(symbol);
        }
    }

    public void remove(String symbol) {
        symbols.remove(symbol.toUpperCase().trim());
    }

    public boolean contains(String symbol) {
        return symbols.contains(symbol.toUpperCase().trim());
    }
}
