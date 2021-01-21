package com.jpereirax.lightorm.codegen;

@FunctionalInterface
public interface Generator<T> {

    T generate();
}
