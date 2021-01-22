package com.jpereirax.lightorm.core.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;

public class DataSource {

    private static DataSource instance;

    private DataSourceConfiguration configuration;
    private HikariDataSource dataSource;

    private DataSource() {}

    public DataSource(DataSourceConfiguration configuration) {
        this.configuration = configuration;
        instance = this;
    }

    public void openConnection() {
        HikariConfig config = new HikariConfig();

        String rawConnectionUrl = configuration.getConnectionType().getUrl();
        String connectionUrl = String.format(rawConnectionUrl, configuration.getHost(), configuration.getPort(), configuration.getDatabase());

        config.setJdbcUrl(connectionUrl);
        config.setUsername(configuration.getUsername());
        config.setPassword(configuration.getPassword());

        config.setMaximumPoolSize(configuration.getMaxPoolSize());

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public Connection getConnection() {
        return dataSource.getConnection();
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }
}
