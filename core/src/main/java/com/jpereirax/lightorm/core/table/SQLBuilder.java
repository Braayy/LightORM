package com.jpereirax.lightorm.core.table;

@FunctionalInterface
public interface SQLBuilder<T> {

    String make(Object... objects);
}
