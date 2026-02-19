package owres.stockcomparer.model.data.database;

import owres.stockcomparer.model.data.IDataProvider;

/**
 * Database is responsible for handling
 * persistent storage of stock price data.
 *
 * This is an abstract placeholder implementation
 * for Sprint 1 and will be extended in later sprints.
 *
 * // NOTE: Actual database implementation will be added in Sprint 2
 */
public class Database implements IDataProvider {

    public Database() {
        // Sprint 1: no database logic implemented
    }

    public void saveStockData() {
        // Placeholder method for saving stock data
    }

    @Override
    public String getData() {
        return "";
    }

    @Override
    public Boolean isAvailable() {
        return false;
    }
}

