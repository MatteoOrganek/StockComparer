package owres.stockcomparer.model.data.Api;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.PriceEntry;
import owres.stockcomparer.model.data.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YahooFinanceAPI implements IDataProvider {

    private static final Logger LOGGER = Logger.getLogger(YahooFinanceAPI.class.getName());

    /**
     * Retrieves historical daily price entries for the given stock between
     * startTime and endTime using the Yahoo Finance API.
     *
     * Returns an empty list if the stock cannot be found or the API is
     * unavailable.
     */
    @Override
    public List<PriceEntry> getData(Stock stock,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime) {
        List<PriceEntry> entries = new ArrayList<>();

        try {
            Calendar from = localDateTimeToCalendar(startTime);
            Calendar to   = localDateTimeToCalendar(endTime);

            yahoofinance.Stock yahooStock =
                    YahooFinance.get(stock.getSymbol(), from, to, Interval.DAILY);

            if (yahooStock == null || yahooStock.getHistory() == null) {
                LOGGER.warning("No data returned for symbol: "
                        + stock.getSymbol());
                return entries;
            }

            for (HistoricalQuote quote : yahooStock.getHistory()) {
                if (quote == null) continue;

                PriceEntry entry = new PriceEntry(
                        calendarToLocalDateTime(quote.getDate()),
                        quote.getOpen()  != null ? quote.getOpen().doubleValue()  : 0.0,
                        quote.getClose() != null ? quote.getClose().doubleValue() : 0.0,
                        quote.getHigh()  != null ? quote.getHigh().doubleValue()  : 0.0,
                        quote.getLow()   != null ? quote.getLow().doubleValue()   : 0.0,
                        quote.getVolume() != null ? quote.getVolume()             : 0L
                );
                entries.add(entry);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to fetch data from Yahoo Finance for symbol: "
                            + stock.getSymbol(), e);
        }

        return entries;
    }

    /**
     * Checks whether the Yahoo Finance API is reachable and the stock symbol
     * is valid. Returns true only if both conditions hold.
     */
    @Override
    public Boolean isAvailable(Stock stock) {
        try {
            yahoofinance.Stock yahooStock =
                    YahooFinance.get(stock.getSymbol());

            return yahooStock != null
                    && yahooStock.isValid()
                    && yahooStock.getQuote() != null;

        } catch (IOException e) {
            LOGGER.log(Level.WARNING,
                    "Yahoo Finance availability check failed for symbol: "
                            + stock.getSymbol(), e);
            return false;
        }
    }

    /**
     * Retrieves the stock exchange on which the given stock is listed,
     * using the exchange suffix returned by Yahoo Finance (e.g. "LSE", "NMS").
     * Returns null if the exchange cannot be determined.
     */
    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        try {
            yahoofinance.Stock yahooStock =
                    YahooFinance.get(stock.getSymbol());

            if (yahooStock == null || yahooStock.getStockExchange() == null) {
                LOGGER.warning("Could not determine exchange for symbol: "
                        + stock.getSymbol());
                return null;
            }

            String exchangeCode = yahooStock.getStockExchange();
            String exchangeName = resolveExchangeName(exchangeCode);

            return new StockExchange(exchangeName, exchangeCode);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to fetch exchange info from Yahoo Finance for symbol: "
                            + stock.getSymbol(), e);
            return null;
        }
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Converts a LocalDateTime to a Calendar instance in the system default
     * time zone, as required by the YahooFinance library.
     */
    private Calendar localDateTimeToCalendar(LocalDateTime ldt) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(
                ldt.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );
        return cal;
    }

    /**
     * Converts a Calendar returned by the YahooFinance library back to a
     * LocalDateTime in the system default time zone.
     */
    private LocalDateTime calendarToLocalDateTime(Calendar cal) {
        if (cal == null) return LocalDateTime.MIN;
        return LocalDateTime.ofInstant(
                cal.toInstant(),
                ZoneId.systemDefault()
        );
    }

    /**
     * Maps Yahoo Finance exchange codes to human-readable names.
     * Extend this map as additional exchanges are required.
     */
    private String resolveExchangeName(String code) {
        switch (code) {
            case "NMS":  return "NASDAQ";
            case "NYQ":  return "New York Stock Exchange";
            case "LSE":  return "London Stock Exchange";
            case "FRA":  return "Frankfurt Stock Exchange";
            case "TYO":  return "Tokyo Stock Exchange";
            default:     return code;
        }
    }
}