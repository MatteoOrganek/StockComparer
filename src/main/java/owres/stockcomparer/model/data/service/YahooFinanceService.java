package owres.stockcomparer.model.data.service;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.stock.PriceEntry;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class YahooFinanceService implements IDataProvider {

    @Override
    public PriceHistory getData(Stock stock,
                                LocalDateTime startTime,
                                LocalDateTime endTime) {

        System.out.println("Trying to load " + stock.getName());

        List<PriceEntry> entries = new ArrayList<>();

        try {
            Calendar from = toCalendar(startTime);
            Calendar to   = toCalendar(endTime);

            yahoofinance.Stock apiStock =
                    YahooFinance.get(stock.getSymbol(), from, to, Interval.DAILY);

            if (apiStock == null || apiStock.getHistory() == null) {
                return empty(stock);
            }

            for (HistoricalQuote quote : apiStock.getHistory()) {
                if (quote == null || quote.getDate() == null) continue;

                entries.add(mapQuote(quote));
            }

        } catch (IOException e) {
            System.out.println("Unable to find YAHOO data.");
            return empty(stock);
        }

        return new PriceHistory(stock, entries);
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        return true;
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        try {
            yahoofinance.Stock apiStock = YahooFinance.get(stock.getSymbol());

            if (apiStock == null || apiStock.getStockExchange() == null) {
                return null;
            }

            String code = apiStock.getStockExchange();
            return new StockExchange(resolveExchangeName(code), code);

        } catch (IOException e) {
            return null;
        }
    }

    // Mapping helpers

    private PriceEntry mapQuote(HistoricalQuote q) {
        return new PriceEntry(
                toLocalDateTime(q.getDate()),
                val(q.getOpen()),
                val(q.getClose()),
                val(q.getHigh()),
                val(q.getLow()),
                q.getVolume() != null ? q.getVolume() : 0L
        );
    }

    private double val(Number n) {
        return n != null ? n.doubleValue() : 0.0;
    }

    private PriceHistory empty(Stock stock) {
        return new PriceHistory(stock, new ArrayList<>());
    }

    private Calendar toCalendar(LocalDateTime ldt) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return cal;
    }

    private LocalDateTime toLocalDateTime(Calendar cal) {
        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
    }

    private String resolveExchangeName(String code) {
        return switch (code) {
            case "NMS" -> "NASDAQ";
            case "NYQ" -> "NYSE";
            case "LSE" -> "London Stock Exchange";
            default -> code;
        };
    }
}