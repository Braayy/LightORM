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
import java.util.stream.Collectors;

import static com.jpereirax.lightorm.codegen.Constants.LINE_BREAK;

public class MethodGenerator implements Generator {

    protected Element element;

    @Override
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        String template = template("method");

        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());

        for (ExecutableElement executableElement : executableElements) {
            String method = template;

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

            parameters = parameters
                    .entrySet()
                    .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue,
                            LinkedHashMap::new
                    ));

            Query query = executableElement.getAnnotation(Query.class);
            Generator generator = BodyGenerator.builder()
                    .query(query)
                    .returnType(returnType)
                    .parameters(parameters)
                    .build();

            method = method
                    .replace("{returnType}", returnType)
                    .replace("{methodName}", methodName)
                    .replace("{parameters}", formattedParameters(parameters))
                    .replace("{body}", generator.generate());

            stringBuilder
                    .append(method)
                    .append(LINE_BREAK);
        }

        return stringBuilder.toString();
    }

    private String formattedParameters(Map<String, String> parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        parameters.forEach((type, name) -> {
            if (stringBuilder.length() > 0) stringBuilder.append(", ");
            stringBuilder.append(String.format("%s %s", type, name));
        });
        return stringBuilder.toString();
    }

    public static MethodGeneratorBuilder builder() {
        return new MethodGeneratorBuilder();
    }
}
