package owres.stockcomparer.model.graph;

import owres.stockcomparer.application.controller.IGraphController;

public class Interaction implements IInteraction {

    IGraphController iGraphController;

    public Interaction(IGraphController iGraphController) {
        this.iGraphController = iGraphController;
    }

    @Override
    public void changeStock(String newStock) {
        IGraph newGraph = new Graph();
        iGraphController.updateGraph(newGraph);
    }

    @Override
    public void changeIndicator(String newIndicator) {

    }
}
