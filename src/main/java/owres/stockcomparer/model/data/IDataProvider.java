package owres.stockcomparer.model.data;

public interface IDataProvider {

    // The function getData will return a JSON formatted string composed of stock data.
    String getData();
    Boolean isAvailable();

}
