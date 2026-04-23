package owres.stockcomparer.model.data.Api;

import java.time.LocalDate;

/**
 * Class for exchange information
 */

//Constructor
public class StockExchange implements IMarketDataSource {

    private String exchangeName;
    private String exchangeCode;

    public StockExchange(String exchangeName, String exchangeCode) {
        this.exchangeName = exchangeName;
        this.exchangeCode = exchangeCode;
    }

    //getters
    public String getExchangeName() {
        return exchangeName;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
