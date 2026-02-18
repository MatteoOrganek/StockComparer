package owres.stockcomparer.model.graph;

import owres.stockcomparer.model.Api.infrastructure.ApiBridge;
import owres.stockcomparer.model.IDataProvider;
import owres.stockcomparer.model.IGraph;
import owres.stockcomparer.model.database.Database;


/**
 * This Class handles the data translation to 1`
 */
public class Graph implements IGraph {

    // Thanks to the Laws of Substitutability, dataProvider can become a database or API instance
    IDataProvider dataProvider;
    String data;

    public String getJSON() {

        // Get database class
        dataProvider = new Database();
        // Try to fetch data using the database
        data = tryFetchData(dataProvider);

        // If the database does not have the correct data, dataProvider is switched to APIBridge
        dataProvider = new ApiBridge();
        if (data == null) tryFetchData(dataProvider);

        // Translate the data to be read by GraphController
        String translatedData = data;

        return translatedData;
    }

    private String tryFetchData(IDataProvider dataProvider) {
        if (dataProvider.isAvailable()){
            // Get data if available
            return dataProvider.getData();
        } else {
            return null;
        }
    }
}
