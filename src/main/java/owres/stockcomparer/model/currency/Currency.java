package owres.stockcomparer.model.currency;

/**
 * CurrencyController defines how different currencies
 * will be supported within the system.
 *
 * This class provides a placeholder for currency handling
 * and conversion logic.
 *
 * NOTE: Actual currency conversion implementation
 * will be added in Sprint 2.
 */
public class Currency {

    public enum SupportedCurrency {
        GBP,
        USD,
        EUR
    }

    public SupportedCurrency getDefaultCurrency() {
        return SupportedCurrency.GBP;
    }

    public double convertPrice(double price, SupportedCurrency targetCurrency) {
        // Placeholder for currency conversion logic
        return price;
    }
}
