package com.jpereirax.lightorm.core.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectionType {

    MySQL("jdbc:mysql://%s:%d/%s"),
    PostgreSQL("jdbc:postgresql://%s:%d/%s");

    private final String url;
}
