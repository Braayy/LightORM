package com.jpereirax.lightorm.core.datasource;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.WeakHashMap;

@Builder
@Getter
public class DataSourceConfiguration {

    private final ConnectionType connectionType;
    private final String host;
    private final long port;
    private final String database;
    private final String username;
    private final String password;

    private final int maxPoolSize;

    private final Map<String, String> properties = new WeakHashMap<>();
}
