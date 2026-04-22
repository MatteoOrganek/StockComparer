package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.data.Company;
import owres.stockcomparer.model.data.Stock;

public interface IInteraction {
    void setCurrentStock(String symbol, String name, Company company);
    Stock getCurrentStock();
    void changeIndicator(String newIndicator);
}
