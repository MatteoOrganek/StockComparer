package infrastructure;

import service.MarketDataSource;
import domain.PricePoint;
import domain.StockSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeMarketDataSource implements MarketDataSource {

    @Override
    public StockSeries fetchDailyCloses(String symbol, LocalDate from, LocalDate to) {

        List<PricePoint> prices = new ArrayList<>();
        LocalDate date = from;
        double value = 100;

        while (!date.isAfter(to)) {
            value += (Math.random() - 0.5);
            prices.add(new PricePoint(date, value));
            date = date.plusDays(1);
        }

        return new StockSeries(symbol, prices);
    }
}
