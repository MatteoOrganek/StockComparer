package owres.stockcomparer.model.data;

import java.time.LocalDateTime;

/**
 * This class creates an instance of price entry, used to track open/close, high/low prices based on a specific time.
 */
public class PriceEntry {

    private LocalDateTime time;
    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
    private long volume;
    private Currency currency;

    /**
     * Base constructor
     * @param time Time
     * @param openPrice OpenPrice
     * @param closePrice ClosePrice
     * @param highPrice HighPrice
     * @param lowPrice LowPrice
     * @param volume Volume
     */
    public PriceEntry(LocalDateTime time, double openPrice, double closePrice, double highPrice, double lowPrice, long volume) {
        this.time = time;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
