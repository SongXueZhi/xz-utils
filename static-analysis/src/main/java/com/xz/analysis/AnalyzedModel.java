package com.xz.analysis;

import com.github.javaparser.ast.CompilationUnit;
import spoon.Launcher;

import java.util.Optional;

public abstract class AnalyzedModel {
   public abstract  Optional<Launcher> asLauncher();
   public abstract Optional<CompilationUnit> asCompilationUnit();
}

