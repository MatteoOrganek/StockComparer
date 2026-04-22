package owres.stockcomparer.model.data.database;

import owres.stockcomparer.model.data.Company;
import owres.stockcomparer.model.data.Stock;

/**
 * Serialisable profile stored at data/profiles/{email}.json
 * Stores email, password, and first watchlist stock as full object.
 */
public class ProfileRecord {

    private String email;
    private String password;
    private Stock firstWatchlistStock;

    // Required by Gson
    public ProfileRecord() {}

    public ProfileRecord(String email, String password, Stock firstWatchlistStock) {
        this.email = email;
        this.password = password;
        this.firstWatchlistStock = firstWatchlistStock;
    }

    public String getEmail()                              { return email; }
    public void setEmail(String email)                    { this.email = email; }

    public String getPassword()                           { return password; }
    public void setPassword(String password)              { this.password = password; }

    public Stock getFirstWatchlistStock()                 { return firstWatchlistStock; }
    public void setFirstWatchlistStock(Stock stock)       { this.firstWatchlistStock = stock; }
}