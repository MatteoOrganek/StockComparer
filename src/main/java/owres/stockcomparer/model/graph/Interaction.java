package owres.stockcomparer.model.graph;

import owres.stockcomparer.application.controller.IGraphController;

public class Interaction implements IInteraction {

    IGraphController iGraphController;


    @Override
    public void changeStock(String newStock) {
        IGraph newGraph = new Graph(newStock);
        iGraphController.renderGraph(newGraph);
    }

    @Override
    public void changeIndicator(String newIndicator) {

    }
}
