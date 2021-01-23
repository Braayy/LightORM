package com.jpereirax.lightorm.core.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectionType {

    H2(
            configuration -> String.format("jdbc:h2:mem:%s", configuration.getDatabase()),
            "org.h2.Driver"
    ),
    MySQL(
            configuration -> defaultUrl("mysql", configuration),
            "com.mysql.cj.jdbc.Driver"
    ),
    PostgreSQL(
            configuration -> defaultUrl("postgresql", configuration),
            "org.postgresql.Driver"
    );

    private final ConnectionURL url;
    private final String driverClass;

    private static String defaultUrl(String driver, DataSourceConfiguration configuration) {
        return String.format("jdbc:%s://%s:%d/%s", driver, configuration.getHost(), configuration.getPort(), configuration.getDatabase());
    }
}
