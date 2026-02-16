package owres.stockcomparer.model;

public interface IData {

    // The function getData will return a JSON formatted string composed of stock data.
    String getData();
    Boolean isAvailable();
    private String fetchData() { return null; }

}
