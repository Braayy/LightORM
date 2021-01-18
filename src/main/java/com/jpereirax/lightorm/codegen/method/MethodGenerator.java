package com.jpereirax.lightorm.codegen.method;

import com.jpereirax.lightorm.annotation.Query;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.codegen.body.BodyGenerator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.*;

import static com.jpereirax.lightorm.codegen.Constants.SINGLE_LINE_BREAK;
import static com.jpereirax.lightorm.codegen.Constants.LINE_BREAK;
import static com.jpereirax.lightorm.codegen.Constants.TAB;

public class MethodGenerator implements Generator {

    protected Element element;

    @Override
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());

        for (ExecutableElement executableElement : executableElements) {
            if (executableElement.getKind() != ElementKind.METHOD) continue;

            String methodName = executableElement.getSimpleName().toString();
            String returnType = executableElement.getReturnType().toString();

            Map<String, String> parameters = new WeakHashMap<>();
            List<? extends VariableElement> variableElements = executableElement.getParameters();
            for (VariableElement parameter : variableElements) {
                String parameterType = parameter.asType().toString();
                String parameterName = parameter.getSimpleName().toString();

                parameters.put(parameterType, parameterName);
            }

            Query query = executableElement.getAnnotation(Query.class);
            Generator generator = BodyGenerator.builder()
                    .query(query)
                    .build();

            stringBuilder
                    .append(TAB)
                    .append(String.format("public %s %s(%s) throws SQLException {", returnType, methodName, formattedParameters(parameters)))
                    .append(SINGLE_LINE_BREAK)
                    .append(generator.generate())
                    .append(SINGLE_LINE_BREAK);
        }

        return stringBuilder.append(TAB).append("}").append(LINE_BREAK).toString();
    }

    private String formattedParameters(Map<String, String> parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        parameters.forEach((type, name) -> stringBuilder.append(String.format("%s %s", type, name)));
        return stringBuilder.toString();
    }

    public static MethodGeneratorBuilder builder() {
        return new MethodGeneratorBuilder();
    }
}
