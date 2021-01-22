package com.jpereirax.lightorm.core.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectionType {

    H2(configuration -> String.format("jdbc:h2:mem:%s", configuration.getDatabase())),
    MySQL(configuration -> defaultUrl("mysql", configuration)),
    PostgreSQL(configuration -> defaultUrl("postgres", configuration));

    private final ConnectionURL url;

    private static String defaultUrl(String driver, DataSourceConfiguration configuration) {
        return String.format("jdbc:%s://%s:%d/%s", driver, configuration.getHost(), configuration.getPort(), configuration.getDatabase());
    }
}
