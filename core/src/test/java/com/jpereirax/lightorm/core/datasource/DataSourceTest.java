package com.jpereirax.lightorm.core.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceTest {

    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        DataSourceConfiguration configuration = DataSourceConfiguration.builder()
                .connectionType(ConnectionType.H2)
                .database("testdb")
                .maxPoolSize(3)
                .build();

        dataSource = new DataSource(configuration);
    }

    @Test
    void whenExecuteGetInstance() {
        DataSource instance = DataSource.getInstance();
        assertAll(
                () -> assertNotNull(instance),
                () -> assertEquals(instance, dataSource)
        );
    }

    @Test
    void whenOpenConnectionShouldReturnConnection() {
        dataSource.openConnection();
        assertAll(
                () -> assertNotNull(dataSource.getConnection())
        );
    }
}