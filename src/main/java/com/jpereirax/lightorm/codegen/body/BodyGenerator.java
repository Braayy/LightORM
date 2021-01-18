package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;
import com.jpereirax.lightorm.codegen.Generator;

import static com.jpereirax.lightorm.codegen.Constants.TAB;
import static com.jpereirax.lightorm.codegen.Constants.SINGLE_LINE_BREAK;
import static com.jpereirax.lightorm.codegen.Constants.LINE_BREAK;

public class BodyGenerator implements Generator {

    protected Query query;

    @Override
    public String generate() {
        String DOUBLE_TAB = TAB + TAB;
        StringBuilder stringBuilder = new StringBuilder(DOUBLE_TAB);

        stringBuilder
                .append(String.format("PreparedStatement preparedStatement = connection.prepareStatement(\"%s\");", query.value()))
                .append(SINGLE_LINE_BREAK)
                .append(DOUBLE_TAB)
                .append("ResultSet resultSet = preparedStatement.executeQuery();")
                .append(LINE_BREAK)
                .append(DOUBLE_TAB)
                .append("return \"\";");

        return stringBuilder.toString();
    }

    public static BodyGeneratorBuilder builder() {
        return new BodyGeneratorBuilder();
    }
}
