package com.xz.examples;

import com.xz.analysis.StaticAnalyst;
import fr.inria.controlflow.ControlFlowGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class ControlFlowExample {
    public static void main(String[] args) {
        StaticAnalyst staticAnalyst = new StaticAnalyst(StaticAnalyst.Core.SPOON);
        staticAnalyst.setLanguageLevel(11);
        ControlFlowGraph graph  = staticAnalyst.buildControlFlowGraphFrom(
                staticAnalyst.createModel("examples/src/test/java"),
                "TestCase1",
                "getN"
        );
        graph.simplify();
        System.out.println(graph.toGraphVisText());
    }
    public static void printGraph(ControlFlowGraph graph) {
        for (DefaultEdge edge : graph.edgeSet()) {
            System.out.println("Edge: " + edge);
        }
    }
}
