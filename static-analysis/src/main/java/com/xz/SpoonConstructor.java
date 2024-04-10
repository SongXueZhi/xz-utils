package com.xz;
import org.jgrapht.Graph;
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
        Launcher launcher = launcherWrapper.asLauncher().orElseThrow(() -> new IllegalArgumentException("wrapper is not a spoon launcher"));
        Graph<String, DefaultEdge> callGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

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

        return  callGraph;
    }


}