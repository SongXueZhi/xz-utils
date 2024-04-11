package com.xz.analysis;

import fr.inria.controlflow.ControlFlowGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public interface Constructor<T extends AnalyzedModel > {
    T createModel(int languageLevel, String...sourcePaths);
    Graph<String, DefaultEdge> createCallGraphFrom(T model, String className, String methodName);
    int checkReachabilityAndDistance(Graph<String, ?> graph, String method1, String method2);
}
