package owres.stockcomparer.model.data.Api.domain;

import java.util.List;

public class StockSeries {
    private final String symbol;
    private final List<PricePoint> prices;

    public StockSeries(String symbol, List<PricePoint> prices) {
        this.symbol = symbol;
        this.prices = prices;
    }

    public String getSymbol() { return symbol; }
    public List<PricePoint> getPrices() { return prices; }
}
