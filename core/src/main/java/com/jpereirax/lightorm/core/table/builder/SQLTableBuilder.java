package com.jpereirax.lightorm.core.table.builder;

import com.jpereirax.lightorm.core.annotation.AutoIncrement;
import com.jpereirax.lightorm.core.annotation.Column;
import com.jpereirax.lightorm.core.annotation.PrimaryKey;
import com.jpereirax.lightorm.core.annotation.Table;
import com.jpereirax.lightorm.core.table.SQLBuilder;
import com.jpereirax.lightorm.core.table.types.SQLTypes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SQLTableBuilder implements SQLBuilder<Object> {

    @Override
    public String make(Object... objects) {
        Class<?> classTable = (Class<?>) objects[0];
        List<Field> fields = (List<Field>) objects[1];

        return String.format("CREATE TABLE IF NOT EXISTS %S(%S)", getTableName(classTable), joinedParams(fields));
    }

    public String joinedParams(List<Field> fields) {
        List<String> params = new ArrayList<>();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Column) {
                    Column column = (Column) annotation;

                    String name = field.getName();
                    if (!column.name().isEmpty()) name = column.name();

                    name = stringToSQLFormatUnderscore(name);

                    SQLTypes type = SQLTypes.findByJavaType(field.getType().getSimpleName());
                    if (type == SQLTypes.DOUBLE && column.precision() > 0) type = SQLTypes.DECIMAL;

                    StringBuilder stringBuilder = new StringBuilder("%S %S");
                    if (type != SQLTypes.INT && type != SQLTypes.DOUBLE) {
                        stringBuilder
                                .append("(%d");

                        if (type == SQLTypes.DECIMAL) stringBuilder.append(", %d");

                        stringBuilder.append(")");
                    }

                    stringBuilder
                            .append(addProperty("_", field, AutoIncrement.class))
                            .append(addProperty(" ", field, PrimaryKey.class));

                    params.add(
                            String.format(
                                    stringBuilder.toString(),
                                    stringToSQLFormatUnderscore(name),
                                    type,
                                    column.length(),
                                    column.precision()
                            )
                    );
                }
            }
        }
        return String.join(", ", params);
    }

    private String addProperty(String delimiter, Field field, Class<? extends Annotation> property) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            String annotationName = annotation.annotationType().getSimpleName();
            String propertyName = property.getSimpleName();

            if (annotationName.equals(propertyName)) {
                return String.format(" %s", stringToSQLFormat(annotationName, delimiter));
            }
        }
        return "";
    }

    private String getTableName(Class<?> klass) {
        Annotation[] annotations = klass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                Table table = (Table) annotation;

                if (!table.name().isEmpty()) {
                    return stringToSQLFormatUnderscore(table.name());
                }
            }
        }
        return stringToSQLFormat(klass.getSimpleName(), "_");
    }

    private String stringToSQLFormatUnderscore(String string) {
        return stringToSQLFormat(string, "_");
    }

    private String stringToSQLFormat(String string, String delimiter) {
        boolean isUpper = string.equals(string.toUpperCase());

        if (!isUpper) {
            List<String> list = Arrays.stream(string.split("(?=[A-Z])")).map(String::toUpperCase).collect(Collectors.toList());
            string = String.join(delimiter, list);
        }

        return string;
    }
}
