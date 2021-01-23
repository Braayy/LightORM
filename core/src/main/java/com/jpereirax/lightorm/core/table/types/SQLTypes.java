package com.jpereirax.lightorm.core.table.types;

import lombok.Getter;

@Getter
public enum SQLTypes {

    VARCHAR(
            "String"
    ),
    INT(
            "Integer", "int"
    ),
    DOUBLE(
            "Double", "double"
    ),
    DECIMAL(
            "Float", "float"
    ),
    DATE(
            "Date"
    ),
    TIMESTAMP(
            "Timestamp"
    );

    private final String[] javaTypes;

    SQLTypes(String... javaTypes) {
        this.javaTypes = javaTypes;
    }

    public static SQLTypes findByJavaType(String javaType) {
        for (SQLTypes types : values()) {
            for (String type : types.getJavaTypes()) {
                if (javaType.equals(type)) return types;
            }
        }
        return SQLTypes.VARCHAR;
    }
}
