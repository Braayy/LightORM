package com.jpereirax.lightorm.core.datasource;

import lombok.Builder;
import lombok.Getter;

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
}
