package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.exception.CodegenException;
import com.jpereirax.lightorm.type.ResultSetTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jpereirax.lightorm.codegen.Constants.SINGLE_LINE_BREAK;

public class BodyGenerator implements Generator {

    protected Query query;
    protected String returnType;
    protected Map<String, String> parameters;

    @Override
    public String generate() {
        StringBuilder template = new StringBuilder(template("body").replace("{statementParams}", setQueryParameters()));
        String rawQuery = query.value().toUpperCase();

        if (!returnType.equals("void")) {
            if (!rawQuery.startsWith("SELECT")) throw new CodegenException("Only SELECT Query can return object.");
            if (!ResultSetTypeEnum.isNativeType(returnType)) throw new CodegenException("Return type not supported.");

            generateBasicReturnType(template);
        }

        return template.toString().replace("{query}", rawQuery);
    }

    private String setQueryParameters() {
        List<String> statementParams = new ArrayList<>();
        long numberOfParams = query.value().chars().filter(it -> it == '?').count();
        if (numberOfParams > 0) {
            if (numberOfParams != parameters.size()) throw new CodegenException("The number of parameters informed is not the same as necessary in the query.");

            AtomicInteger index = new AtomicInteger(1);
            parameters.forEach((type, name) -> {
                if (!ResultSetTypeEnum.isNativeType(type)) throw new CodegenException(String.format("Parameter type not supported: %s.", type));

                ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(type);
                String currentIndex = Integer.toString(index.getAndIncrement());

                String setMethod = resultSetType.setMethod()
                        .replace("?1", currentIndex)
                        .replace("?2", name);

                statementParams.add(String.format("%s;", setMethod));
            });
        }
        return String.join(SINGLE_LINE_BREAK, statementParams);
    }

    private void generateBasicReturnType(StringBuilder template) {
        ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(returnType);
        template
                .append(String.format("return %s;", resultSetType.getMethod().replace("?", "1")));
    }

    public static BodyGeneratorBuilder builder() {
        return new BodyGeneratorBuilder();
    }
}
