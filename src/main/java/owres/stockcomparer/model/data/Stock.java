package owres.stockcomparer.model.data;

/**
 * This class creates a stock objects defined by its symbol, name, company
 */
public class Stock implements IStock {

    private String symbol;
    private String name;
    private Company company;

    public Stock(String symbol, String name, Company company) {
        this.symbol = symbol;
        this.name = name;
        this.company = company;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
