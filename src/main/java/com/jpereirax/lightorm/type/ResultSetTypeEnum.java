package com.jpereirax.lightorm.type;

import java.util.Arrays;

public enum ResultSetTypeEnum {

    INT("rs.getInt(?)", "int", "Integer"),
    DOUBLE("rs.getDouble(?)", "double", "Double"),
    STRING("rs.getString(?)", "String"),
    LONG("rs.getLong(?)", "double", "Double"),
    FLOAT("rs.getFloat(?)", "float", "Float"),
    BOOLEAN("rs.getBoolean(?)", "boolean", "Boolean");

    private final String method;
    private final String[] types;

    ResultSetTypeEnum(String method, String... types) {
        this.method = method;
        this.types = types;
    }

    public static boolean isNativeType(String returnType) {
        String[] split = returnType.split("\\.");
        return Arrays.stream(values()).anyMatch(it -> Arrays.asList(it.types).contains(split[split.length - 1]));
    }

    public static ResultSetTypeEnum getResultSetTypeFromReturnType(String returnType) {
        for (ResultSetTypeEnum value : values()) {
            for (String types : value.types) {
                if (returnType.toLowerCase().endsWith(types.toLowerCase())) {
                    return value;
                }
            }
        }
        return STRING;
    }

    public String getMethod() {
        return method;
    }
}
