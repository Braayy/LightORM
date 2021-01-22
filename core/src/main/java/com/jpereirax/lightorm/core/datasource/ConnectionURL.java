package com.jpereirax.lightorm.core.datasource;

@FunctionalInterface
public interface ConnectionURL {

    String make(DataSourceConfiguration configuration);
}
