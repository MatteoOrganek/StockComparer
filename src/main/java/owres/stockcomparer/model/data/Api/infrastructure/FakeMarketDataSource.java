package owres.stockcomparer.model.data.Api.infrastructure;

import owres.stockcomparer.model.data.Api.service.IMarketDataSource;
import owres.stockcomparer.model.data.Api.domain.PricePoint;
import owres.stockcomparer.model.data.Api.domain.StockSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeMarketDataSource implements IMarketDataSource {

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
