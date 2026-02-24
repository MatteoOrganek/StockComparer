package owres.stockcomparer.application.controller;

import owres.stockcomparer.model.graph.Graph;
import owres.stockcomparer.model.graph.IGraph;

public interface IGraphController {
    void renderGraph(IGraph graph);
}
