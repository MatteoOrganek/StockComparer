package owres.stockcomparer;

import owres.stockcomparer.model.graph.Graph;
import owres.stockcomparer.model.graph.IGraph;

public class Main {
    public static void main(String[] args) {
        // Database and profile storage smoke test
        // Remove before final submission if Launcher is the entry point
        IGraph graph = new Graph();
        System.out.println(graph.getData());
    }
}