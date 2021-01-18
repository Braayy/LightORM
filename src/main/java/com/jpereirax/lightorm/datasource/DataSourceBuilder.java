package com.jpereirax.lightorm.datasource;

public class DataSourceBuilder {

    private final DataSource connection;

    public DataSourceBuilder() {
        this.connection = new DataSource();
    }

    public DataSourceBuilder dataSourceClassName(String dataSourceClassName) {
        this.connection.dataSourceClassName = dataSourceClassName;
        return this;
    }

    public DataSourceBuilder url(String url) {
        this.connection.url = url;
        return this;
    }

    public DataSourceBuilder username(String username) {
        this.connection.username = username;
        return this;
    }

    public DataSourceBuilder password(String password) {
        this.connection.password = password;
        return this;
    }

    public DataSource build() {
        return this.connection;
    }
}
