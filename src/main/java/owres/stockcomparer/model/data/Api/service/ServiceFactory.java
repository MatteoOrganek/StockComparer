package owres.stockcomparer.model.data.Api.service;

import owres.stockcomparer.model.data.Api.infrastructure.AlphaVantageMarketDataSource;

public class ServiceFactory {

    private static final String API_KEY = System.getenv("APIKEY");

    public static StockPriceService createStockPriceService() {
        return new StockPriceService(
                new AlphaVantageMarketDataSource(API_KEY)
        );
    }
}
