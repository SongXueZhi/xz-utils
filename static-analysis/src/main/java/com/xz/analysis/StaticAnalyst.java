package com.xz.analysis;
import fr.inria.controlflow.ControlFlowGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
public class StaticAnalyst {
    private int languageLevel;
    private final Constructor<? extends AnalyzedModel> constructor;

    public int getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(int languageLevel) {
        this.languageLevel = languageLevel;
    }

    public enum Core {
        SPOON,
        JAVA_PARSER
    }
    public StaticAnalyst(Core core) {
        switch (core) {
            case SPOON:
                this.constructor = new SpoonConstructor();
                break;
            case JAVA_PARSER:
                this.constructor = new JavaParserConstructor();
                break;
            default:
                throw new IllegalArgumentException("Unsupported core: " + core);
        }
    }

    public AnalyzedModel createModel(String...sourcePaths) {
        return (AnalyzedModel) constructor.createModel(languageLevel, sourcePaths);
    }

    public Graph<String, DefaultEdge> createCallGraphFrom(AnalyzedModel model, String className, String methodName) {
        if (constructor instanceof SpoonConstructor)
            return ((SpoonConstructor)constructor).createCallGraphFrom((LauncherWrapper) model, className, methodName);
        else if (constructor instanceof JavaParserConstructor)
            return ((JavaParserConstructor)constructor).createCallGraphFrom((CompilationUnitWrapper) model, className, methodName);
        return null;
    }

    public ControlFlowGraph buildControlFlowGraphFrom(AnalyzedModel model, String className, String methodName) {
        if (constructor instanceof SpoonConstructor)
            return ((SpoonConstructor)constructor).buildControlFlowGraph((LauncherWrapper) model, className, methodName);
        return null;
    }
    public  int checkReachabilityAndDistance(Graph<String, ?> graph, String method1, String method2) {
        return constructor.checkReachabilityAndDistance(graph, method1, method2);
    }
}
