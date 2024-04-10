package com.xz.analysis;

import com.github.javaparser.ast.CompilationUnit;
import spoon.Launcher;

import java.util.Optional;

public class CompilationUnitWrapper extends AnalyzedModel {
    private final CompilationUnit compilationUnit;

    public CompilationUnitWrapper(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }
    @Override
    public Optional<Launcher> asLauncher() {
        return Optional.empty();
    }

    @Override
    public Optional<CompilationUnit> asCompilationUnit() {
        return Optional.of(compilationUnit);
    }
}
