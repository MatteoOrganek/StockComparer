package owres.stockcomparer.model.data.database;

/**
 * Serialisable profile stored at data/profiles/{email}.json
 * Keeps email, password, and the first watchlist stock symbol.
 */
public class ProfileRecord {

    private String email;
    private String password;
    private String firstWatchlistSymbol; // e.g. "TSLA"

    // Required by Jackson
    public ProfileRecord() {}

    public ProfileRecord(String email, String password, String firstWatchlistSymbol) {
        this.email = email;
        this.password = password;
        this.firstWatchlistSymbol = firstWatchlistSymbol;
    }

    public String getEmail()                             { return email; }
    public void setEmail(String email)                   { this.email = email; }

    public String getPassword()                          { return password; }
    public void setPassword(String password)             { this.password = password; }

    public String getFirstWatchlistSymbol()              { return firstWatchlistSymbol; }
    public void setFirstWatchlistSymbol(String symbol)   { this.firstWatchlistSymbol = symbol; }
}