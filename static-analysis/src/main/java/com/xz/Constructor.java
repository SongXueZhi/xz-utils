package com.xz;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public interface Constructor<T extends AnalyzedModel > {
    T createModel(int languageLevel, String...sourcePaths);
    Graph<String, DefaultEdge> createCallGraphFrom(T model, String className, String methodName);
}
