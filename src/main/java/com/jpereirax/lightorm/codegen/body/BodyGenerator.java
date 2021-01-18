package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.exception.CodegenException;
import com.jpereirax.lightorm.type.ResultSetTypeEnum;

public class BodyGenerator implements Generator {

    protected Query query;
    protected String returnType;

    @Override
    public String generate() {
        StringBuilder template = new StringBuilder(template("body"));
        String rawQuery = query.value().toUpperCase();

        if (!returnType.equals("void")) {
            if (!rawQuery.startsWith("SELECT")) throw new CodegenException("Only SELECT Query can return object.");
            if (!ResultSetTypeEnum.isNativeType(returnType)) throw new CodegenException("Return type not supported.");

            ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromReturnType(returnType);
            template
                    .append(String.format("return %s;", resultSetType.getMethod().replace("?", "1")));
        }

        return template.toString().replace("{query}", rawQuery);
    }

    public static BodyGeneratorBuilder builder() {
        return new BodyGeneratorBuilder();
    }
}
