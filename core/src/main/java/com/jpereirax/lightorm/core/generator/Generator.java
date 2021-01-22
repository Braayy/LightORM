package com.jpereirax.lightorm.core.generator;

@FunctionalInterface
public interface Generator<T> {

    T generate();
}
