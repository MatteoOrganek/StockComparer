package owres.stockcomparer.model.data;

public interface IStock {
    String getSymbol();
    String getName();
    Company getCompany();
    void setSymbol(String symbol);
    void setName(String name);
    void setCompany(Company company);

}
