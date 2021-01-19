package com.jpereirax.lightorm.codegen;

import com.jpereirax.lightorm.annotation.Query;
import lombok.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

@Builder
public class MethodGenerator implements Generator {

    private final Types typeUtils;
    private final Elements elementUtils;

    private final Element element;

    @Override
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        String template = template("method");

        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());

        for (ExecutableElement executableElement : executableElements) {
            String method = template;

            if (executableElement.getKind() != ElementKind.METHOD) continue;

            String methodName = executableElement.getSimpleName().toString();

            TypeMirror returnType = executableElement.getReturnType();
            Element returnElement = typeUtils.asElement(returnType);

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
                    .typeUtils(typeUtils)
                    .query(query)
                    .returnType(returnType)
                    .parameters(parameters)
                    .returnElement(returnElement)
                    .build();

            method = method
                    .replace("{returnType}", returnType.toString())
                    .replace("{methodName}", methodName)
                    .replace("{parameters}", formattedParameters(parameters))
                    .replace("{body}", generator.generate());

            stringBuilder
                    .append(method);
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
}
