package com.xz.analysis;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class JavaParserConstructor implements Constructor<CompilationUnitWrapper>{

    @Override
    public CompilationUnitWrapper createModel(int languageLevel, String... sourcePaths) {
        return null;
    }

    @Override
    public Graph<String, DefaultEdge> createCallGraphFrom(CompilationUnitWrapper model, String className, String methodName) {
        return null;
    }

    @Override
    public int checkReachabilityAndDistance(Graph<String, ?> graph, String method1, String method2) {
        return -1;
    }
}
