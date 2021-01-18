package com.jpereirax.lightorm.type;

import java.util.Arrays;

public enum ResultSetTypeEnum {

    INT("rs.getInt(?)", "statement.setInt(?1, ?2)", "int", "Integer"),
    DOUBLE("rs.getDouble(?)", "statement.setDouble(?1, ?2)", "double", "Double"),
    STRING("rs.getString(?)", "statement.setString(?1, ?2)", "String"),
    LONG("rs.getLong(?)", "statement.setDouble(?1, ?2)", "double", "Double"),
    FLOAT("rs.getFloat(?)", "statement.setFloat(?1, ?2)", "float", "Float"),
    BOOLEAN("rs.getBoolean(?)", "statement.setBoolean(?1, ?2)", "boolean", "Boolean");

    private final String getMethod;
    private final String setMethod;
    private final String[] types;

    ResultSetTypeEnum(String getMethod, String setMethod, String... types) {
        this.getMethod = getMethod;
        this.setMethod = setMethod;
        this.types = types;
    }

    public static boolean isNativeType(String returnType) {
        String[] split = returnType.split("\\.");
        return Arrays.stream(values()).anyMatch(it -> Arrays.asList(it.types).contains(split[split.length - 1]));
    }

    public static ResultSetTypeEnum getResultSetTypeFromType(String returnType) {
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
        return getMethod;
    }

    public String setMethod() {
        return setMethod;
    }
}
