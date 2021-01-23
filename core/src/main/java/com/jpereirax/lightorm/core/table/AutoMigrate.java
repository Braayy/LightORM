package com.jpereirax.lightorm.core.table;

import com.jpereirax.lightorm.core.annotation.Column;
import com.jpereirax.lightorm.core.annotation.Table;
import com.jpereirax.lightorm.core.datasource.DataSource;
import com.jpereirax.lightorm.core.table.builder.SQLTableBuilder;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoMigrate {

    private final DataSource instance = DataSource.getInstance();
    private final List<Class<?>> tables;

    public AutoMigrate() {
        this.tables = new ArrayList<>();
    }

    public void migrate() throws SQLException {
        analysisProject();

        SQLBuilder<Object> tableBuilder = new SQLTableBuilder();

        for (Class<?> table : tables) {
            String query = tableBuilder.make(table, getColumns(table));
            System.out.println(query);

            instance.getConnection()
                    .prepareStatement(query)
                    .executeUpdate();
        }
    }

    private void analysisProject() {
        Reflections reflections = new Reflections();
        tables.addAll(reflections.getTypesAnnotatedWith(Table.class));
    }

    private List<Field> getColumns(Class<?> klass) {
        List<Field> columns = new ArrayList<>();

        Field[] fields = klass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Column) {
                    columns.add(field);
                }
            }
        }

        return columns;
    }
}
