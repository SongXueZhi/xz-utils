package com.xz.analysis;
import fr.inria.controlflow.ControlFlowBuilder;
import fr.inria.controlflow.ControlFlowGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import spoon.Launcher;
import org.jgrapht.graph.DefaultEdge;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class SpoonConstructor implements Constructor<LauncherWrapper> {

    @Override
    public LauncherWrapper createModel(int languageLevel, String... sourcePaths) {
        Launcher launcher = new Launcher();
        for (String sourcePath : sourcePaths) {
            launcher.addInputResource(sourcePath);
        }
        launcher.getEnvironment().setComplianceLevel(languageLevel);
        launcher.buildModel();
        return new LauncherWrapper(launcher);
    }

    @Override
    public Graph<String, DefaultEdge> createCallGraphFrom(LauncherWrapper launcherWrapper, String className, String methodName) {

        // Create a new directed graph where each vertex is a string representing a fully qualified method signature
        Graph<String, DefaultEdge> callGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Generate the call graph using the provided launcherWrapper, class name, and method name
        Graph<CtMethod<?>, DefaultEdge> graph = createCallGraph(launcherWrapper, className, methodName);

        // Convert the graph from CtMethod nodes to String nodes containing fully qualified method signatures
        for (CtMethod<?> method : graph.vertexSet()) {
            String fullyQualifiedSignature = method.getDeclaringType().getQualifiedName() + "#" + method.getSignature();
            callGraph.addVertex(fullyQualifiedSignature);
        }
        for (DefaultEdge edge : graph.edgeSet()) {
            CtMethod<?> source = graph.getEdgeSource(edge);
            CtMethod<?> target = graph.getEdgeTarget(edge);
            String sourceSignature = source.getDeclaringType().getQualifiedName() + "#" + source.getSignature();
            String targetSignature = target.getDeclaringType().getQualifiedName() + "#" + target.getSignature();
            callGraph.addEdge(sourceSignature, targetSignature);
        }

        // Return the call graph with fully qualified method signatures
        return callGraph;
    }

    public Graph<CtMethod<?>, DefaultEdge> createCallGraph(LauncherWrapper launcherWrapper, String className, String methodName) {
        Launcher launcher = launcherWrapper.asLauncher().orElseThrow(() -> new IllegalArgumentException("wrapper is not a spoon launcher"));

        CtClass<?> clazz = launcher.getFactory().Class().get(className);
        CtMethod<?> testMethod = clazz.getMethodsByName(methodName).get(0);

        // 创建JGraphT图
        Graph<CtMethod<?>, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        Set<CtMethod<?>> visitedMethods = new HashSet<>();
        Queue<CtMethod<?>> toVisit = new LinkedList<>();

        // 初始化队列
        toVisit.add(testMethod);

        // 构建调用图
        while (!toVisit.isEmpty()) {
            CtMethod<?> currentMethod = toVisit.remove();
            if (!visitedMethods.contains(currentMethod)) {
                visitedMethods.add(currentMethod);
                graph.addVertex(currentMethod);

                List<CtInvocation<?>> invocations = currentMethod.getElements(new TypeFilter<>(CtInvocation.class));
                for (CtInvocation<?> invocation : invocations) {
                    CtMethod<?> targetMethod = (CtMethod<?>) invocation.getExecutable().getDeclaration();
                    if (targetMethod != null && !visitedMethods.contains(targetMethod)) {
                        graph.addVertex(targetMethod);
                        graph.addEdge(currentMethod, targetMethod);
                        toVisit.add(targetMethod);
                    }
                }
            }
        }
        return  graph;
    }

    public ControlFlowGraph buildControlFlowGraph(LauncherWrapper launcherWrapper, String className, String methodName) {
        Launcher launcher = launcherWrapper.asLauncher().orElseThrow(() -> new IllegalArgumentException("wrapper is not a spoon launcher"));
        ControlFlowBuilder builder = new ControlFlowBuilder();
        CtClass<?> clazz = launcher.getFactory().Class().get(className);
        CtMethod<?> testMethod = clazz.getMethodsByName(methodName).get(0);

        return builder.build(testMethod);
    }

    @Override
    public  int checkReachabilityAndDistance(Graph<String, ?> graph, String method1, String method2) {
        BFSShortestPath<String, ?> bfs = new BFSShortestPath<>(graph);
        GraphPath<String, ?> path = bfs.getPath(method1, method2);

        if (path != null) {
            // 返回路径长度
            return path.getLength();
        } else {
            // 如果没有路径，返回 -1 表示不可达
            return -1;
        }
    }
}