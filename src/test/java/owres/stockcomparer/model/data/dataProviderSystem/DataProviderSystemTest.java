package owres.stockcomparer.model.data.dataProviderSystem;

import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.database.Database;
import owres.stockcomparer.model.data.service.StockExchange;
import owres.stockcomparer.model.stock.Company;
import owres.stockcomparer.model.stock.PriceHistory;
import owres.stockcomparer.model.stock.Stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for DataProviderSystem.
 * Focus: delegation + fallback + validation safety.
 */
class DataProviderSystemTest {

    private Database mockDb;
    private IDataProvider mockOnline;
    private DataProviderSystem system;
    private Stock stock;

    @BeforeEach
    void setup() {
        mockDb = mock(Database.class);
        mockOnline = mock(IDataProvider.class);

        system = new DataProviderSystem(mockDb, mockOnline);

        stock = new Stock("AAPL", "Apple", new Company("Apple"));
    }


    /**
     * Online returns null → system should also return null
     */
    @Test
    void getData_returnsNull_whenNoDataAvailable() {

        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();

        when(mockOnline.getData(stock, start, end)).thenReturn(null);

        PriceHistory result = system.getData(stock, start, end);

        assertNull(result);
    }

    /**
     * Very large invalid range (over 2 years)
     * should be rejected by filter layer
     */
    @Test
    void getData_returnsNull_whenRangeTooLarge() {

        LocalDateTime start = LocalDateTime.now().minusYears(3);
        LocalDateTime end = LocalDateTime.now();

        PriceHistory result = system.getData(stock, start, end);

        assertNull(result);

        verifyNoInteractions(mockOnline);
        verifyNoInteractions(mockDb);
    }

}