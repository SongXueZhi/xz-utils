package com.xz;

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
}
