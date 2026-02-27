package owres.stockcomparer.model.data.Api;


import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.IStockData;

import java.time.LocalDate;

public class ApiBridge implements IDataProvider {

    IMarketDataSource marketDataSource;

    @Override
    public String getData() {
        return "";
    }

    @Override
    public Boolean isAvailable() {
        return null;
    }
}
