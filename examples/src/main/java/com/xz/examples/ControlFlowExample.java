package com.xz.examples;

import com.xz.analysis.StaticAnalyst;
import fr.inria.controlflow.ControlFlowGraph;

public class ControlFlowExample {
    public static void main(String[] args) {
        StaticAnalyst staticAnalyst = new StaticAnalyst(StaticAnalyst.Core.SPOON);
        staticAnalyst.setLanguageLevel(11);
        ControlFlowGraph graph  = staticAnalyst.buildControlFlowGraphFrom(
                staticAnalyst.createModel("examples/src/test/java"),
                "TestCase1",
                "getN"
        );
        System.out.println(graph.toGraphVisText());
    }
}
