package owres.stockcomparer.model.data.Api;

import owres.stockcomparer.model.data.PriceEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeMarketDataSource implements IMarketDataSource {

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
       //Creates empty list
        List<PriceEntry> entries = new ArrayList<>();
        //Creates random number generator
        Random random = new Random();
        //Gives the fake stock a random starting point between 100-150 before daily price changes begin
        double currentPrice = 100.0 + random.nextDouble() * 50.0;

        LocalDate currentDate = from;

        //Loop until end date, generating one fake trading day at a time
        while (!currentDate.isAfter(to)) {
            //day opens at current price
            double open = currentPrice;
            //Creates closing price by moving stock up or down slightly
            double close = open + (random.nextDouble() * 4 - 2);
            //the high should be at least as high as open or close
            double high = Math.max(open, close) + random.nextDouble() * 2;
            //the low should be at least as low as open or close
            double low = Math.min(open, close) - random.nextDouble() * 2;
            //fake trading volume
            long volume = 100_000 + random.nextInt(900_000);

            //creates PriceEntry object and adds it to list with fixed time of midday
            entries.add(new PriceEntry(
                    LocalDateTime.of(currentDate, java.time.LocalTime.NOON),
                    open,
                    close,
                    high,
                    low,
                    volume
            ));

            //fake simulation forward - tomorrow starts from todays closing price and move to the next day
            currentPrice = close;
            currentDate = currentDate.plusDays(1);
        }

        //This returns all the fake data together
        return new StockPriceDataSource(symbol, entries);
    }
}


