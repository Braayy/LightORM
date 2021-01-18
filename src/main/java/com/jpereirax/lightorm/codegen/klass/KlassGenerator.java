package com.jpereirax.lightorm.codegen.klass;

import com.jpereirax.lightorm.codegen.Constants;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.codegen.method.MethodGenerator;

import javax.lang.model.element.Element;

import static com.jpereirax.lightorm.codegen.Constants.LINE_BREAK;
import static com.jpereirax.lightorm.codegen.Constants.TAB;

public class KlassGenerator implements Generator {

    protected String packageName;
    protected String className;
    protected Element element;

    @Override
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(String.format("package %s;", packageName))
                .append(LINE_BREAK);

        for (String defaultImport : Constants.DEFAULT_IMPORTS) {
            stringBuilder
                    .append(defaultImport)
                    .append("\n");
        }

        stringBuilder
                .append("\n")
                .append(String.format("public class %s {", className))
                .append(LINE_BREAK)
                .append(TAB)
                .append("private final Connection connection = DataSource.getConnection();")
                .append(LINE_BREAK);

        Generator methodGenerator = MethodGenerator.builder()
                .element(element)
                .build();
        stringBuilder.append(methodGenerator.generate());

        return stringBuilder.append("}").toString();
    }

    public static KlassGeneratorBuilder builder() {
        return new KlassGeneratorBuilder();
    }
}
