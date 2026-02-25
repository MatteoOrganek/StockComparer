package owres.stockcomparer.profile;

public class CompanyProfile {

    private final String symbol;
    private final String name;
    private final String sector;
    private final String description;

    public CompanyProfile(String symbol, String name, String sector, String description) {
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
        this.description = description;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getSector() { return sector; }
    public String getDescription() { return description; }
}