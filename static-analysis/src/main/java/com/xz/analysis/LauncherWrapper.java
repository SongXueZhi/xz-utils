package com.xz.analysis;

import com.github.javaparser.ast.CompilationUnit;
import spoon.Launcher;

import java.util.Optional;

// 使模型类实现这个接口
public class LauncherWrapper extends AnalyzedModel {
    private final Launcher launcher;

    public LauncherWrapper(Launcher launcher) {
        this.launcher = launcher;
    }
    @Override
    public Optional<Launcher> asLauncher() {
        return Optional.of(launcher);
    }

    @Override
    public Optional<CompilationUnit> asCompilationUnit() {
        return Optional.empty();
    }
    // 实现接口方法
}
