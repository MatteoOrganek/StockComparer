package service;

import infrastructure.AlphaVantageMarketDataSource;

public class ServiceFactory {

    private static final String API_KEY = "5CALU5Z7IE7KXZI7";

    public static StockPriceService createStockPriceService() {
        return new StockPriceService(
                new AlphaVantageMarketDataSource(API_KEY)
        );
    }
}
