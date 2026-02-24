package owres.stockcomparer.model.data.Api.infrastructure;

import owres.stockcomparer.model.data.Api.domain.StockSeries;
import owres.stockcomparer.model.data.Api.service.ServiceFactory;
import owres.stockcomparer.model.data.Api.service.StockPriceService;
import owres.stockcomparer.model.data.IDataProvider;

import java.time.LocalDate;

public class ApiBridge implements IDataProvider {
    public static void main(String[] args) {
        try {
            StockPriceService stockService = ServiceFactory.createStockPriceService();

            StockSeries series = stockService.getPrices(
                    "AAPL",
                    LocalDate.now().minusDays(30),
                    LocalDate.now()
            );

            System.out.println("OK: " + series.getSymbol() + " points=" + series.getPrices().size());
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getData() {
        return "Data from API";
    }

    @Override
    public Boolean isAvailable() {
        return true;
    }
}
