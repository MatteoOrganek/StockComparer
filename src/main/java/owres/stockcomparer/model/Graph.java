package owres.stockcomparer.model;

public class Graph implements IGraph {

    IData dataProvider;

    private void init() {
        // TODO Check if database has up to date data, if so, Database will be used to provide data
        // If not, dataProvider will become APIBridge

        dataProvider.getData();
    }
}
