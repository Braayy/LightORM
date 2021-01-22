package com.jpereirax.lightorm.codegen;

import com.jpereirax.lightorm.core.annotation.Query;
import com.jpereirax.lightorm.core.generator.Generator;
import com.jpereirax.lightorm.type.ParameterType;
import com.squareup.javapoet.*;
import lombok.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Builder
public class MethodGenerator implements Generator<List<MethodSpec>> {

    private final ProcessingEnvironment processingEnvironment;
    private final Element element;

    @Override
    public List<MethodSpec> generate() {
        List<MethodSpec> methods = new ArrayList<>();

        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());
        for (ExecutableElement executableElement : executableElements) {
            if (executableElement.getKind() != ElementKind.METHOD) continue;

            String methodName = executableElement.getSimpleName().toString();
            TypeMirror returnType = executableElement.getReturnType();
            TypeName returnTypeName = ParameterizedTypeName.get(returnType);
            Element returnElement = processingEnvironment.getTypeUtils().asElement(returnType);

            Query query = executableElement.getAnnotation(Query.class);
            List<ParameterSpec> methodParams = methodParams(executableElement);
            List<ParameterType> codeBlockParams = codeBlockParams(executableElement);

            Generator<CodeBlock> codeBlockGenerator = CodeBlockGenerator.builder()
                    .processingEnvironment(processingEnvironment)
                    .query(query)
                    .returnType(returnType)
                    .parameters(codeBlockParams)
                    .returnElement(returnElement)
                    .build();

            MethodSpec method = MethodSpec
                    .methodBuilder(methodName)
                    .addAnnotation(Override.class)
                    .addException(SQLException.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnTypeName)
                    .addParameters(methodParams)

                    .addCode(codeBlockGenerator.generate())

                    .build();

            methods.add(method);
        }

        return methods;
    }

    private List<ParameterSpec> methodParams(ExecutableElement executableElement) {
        return executableElement.getParameters()
                .stream()
                .map(param ->
                        ParameterSpec
                                .builder(
                                        ParameterizedTypeName.get(param.asType()),
                                        param.getSimpleName().toString()
                                )
                                .build())
                .collect(Collectors.toList());
    }

    private List<ParameterType> codeBlockParams(ExecutableElement executableElement) {
        List<ParameterType> parameters = new ArrayList<>();
        List<? extends VariableElement> variableElements = executableElement.getParameters();
        for (VariableElement parameter : variableElements) {
            String parameterType = parameter.asType().toString();
            String parameterName = parameter.getSimpleName().toString();

            parameters.add(
                    ParameterType.builder()
                            .type(parameterType)
                            .name(parameterName)
                            .build()
            );
        }

        return parameters;
    }
}
