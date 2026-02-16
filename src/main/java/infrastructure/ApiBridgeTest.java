package infrastructure;

import domain.StockSeries;
import service.ServiceFactory;
import service.StockPriceService;

import java.time.LocalDate;

public class ApiBridgeTest {
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
}
