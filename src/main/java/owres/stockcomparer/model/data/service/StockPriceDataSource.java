package owres.stockcomparer.model.data.service;

import owres.stockcomparer.model.stock.PriceEntry;

import java.time.LocalDate;
import java.util.List;

public class StockPriceDataSource implements IMarketDataSource {
    //Stock Symbol
    private final String symbol;
    //Stores daily stock price records for symbol
    private final List<PriceEntry> entries;

    //constructer
    public StockPriceDataSource(String symbol, List<PriceEntry> entries) {
        this.symbol = symbol;
        this.entries = entries;
    }

    //getter
    public String getSymbol() {
        return symbol;
    }

    public List<PriceEntry> getEntries() {
        return entries;
    }

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
