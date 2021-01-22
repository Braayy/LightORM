package com.jpereirax.lightorm.codegen;

import com.jpereirax.lightorm.core.annotation.Query;
import com.jpereirax.lightorm.core.generator.Generator;
import com.jpereirax.lightorm.exception.CodegenException;
import com.jpereirax.lightorm.type.ParameterType;
import com.jpereirax.lightorm.type.ResultSetTypeEnum;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Builder
public class CodeBlockGenerator implements Generator<CodeBlock> {

    private final ProcessingEnvironment processingEnvironment;

    private final Query query;
    private final TypeMirror returnType;
    private final List<ParameterType> parameters;

    private final Element returnElement;
    
    private CodeBlock.Builder codeBlock;

    @Override
    public CodeBlock generate() {
        String rawQuery = query.value().toUpperCase();
        String rawReturnType = returnType.toString();

        codeBlock = CodeBlock.builder()
                .addStatement("$T statement = connection.prepareStatement($S)", PreparedStatement.class, rawQuery);

        setQueryParameters(rawQuery);

        if (!rawReturnType.equals("void")) {
            if (!rawQuery.startsWith("SELECT")) throw new CodegenException("Only SELECT Query can return object.");

            codeBlock
                    .addStatement("$T rs = statement.executeQuery()", ResultSet.class);

            if (ResultSetTypeEnum.isNativeType(rawReturnType)) {
                addSimpleReturn(rawReturnType);
                return codeBlock.build();
            }

            addComplexReturn(rawReturnType);
        } else {
            codeBlock
                    .addStatement("statement.executeUpdate()");
        }

        return codeBlock.build();
    }

    private void addComplexReturn(String rawReturnType) {
        boolean returnList = rawReturnType.startsWith("java.util.List");
        List<ExecutableElement> executableElements = ElementFilter.constructorsIn(returnElement.getEnclosedElements());

        String returnObject = rawReturnType;
        if (returnList) {
            DeclaredType declaredReturnType = (DeclaredType) returnType;
            TypeMirror returnObjectType = declaredReturnType.getTypeArguments().get(0);

            Element returnObjectElement = processingEnvironment.getTypeUtils().asElement(returnObjectType);
            executableElements = ElementFilter.constructorsIn(returnObjectElement.getEnclosedElements());

            returnObject = returnObjectType.toString();

            TypeName responseList = ParameterizedTypeName.get(returnType);
            ClassName arrayList = ClassName.get("java.util", "ArrayList");

            codeBlock
                    .addStatement("$T response = new $T<>()", responseList, arrayList)
                    .beginControlFlow("while(rs.next())");
        }

        if (executableElements.isEmpty()) throw new CodegenException(String.format("Cannot find constructor: %s.", returnElement.getSimpleName()));

        List<String> parametersName = new ArrayList<>();

        ExecutableElement executableElement = executableElements.get(0);
        List<? extends VariableElement> parameters = executableElement.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            VariableElement parameter = parameters.get(i);

            String type = parameter.asType().toString();
            String name = parameter.getSimpleName().toString();

            ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(type);

            String[] primitiveTypes = resultSetType.getTypes();
            String defaultType = primitiveTypes[primitiveTypes.length - 1];
            ClassName primitiveClass = ClassName.get("java.lang", defaultType);

            parametersName.add(String.format("_%s", name));
            codeBlock
                    .addStatement("$T _$L = new $T($L($L))", primitiveClass, name, primitiveClass, resultSetType.getMethod(), i + 1);
        }

        ClassName returnClass = ClassName.get(getPackageName(returnObject), getClassName(returnObject));

        List<CodeBlock> params = parametersName.stream().map(CodeBlock::of).collect(Collectors.toList());
        CodeBlock joinedParams = CodeBlock.join(params, ", ");

        codeBlock
                .addStatement("$T object = new $T($L)", returnClass, returnClass, joinedParams);

        if (returnList) {
            codeBlock
                    .addStatement("response.add(object)")
                    .endControlFlow()
                    .addStatement("return response");

            return;
        }

        codeBlock
                .addStatement("return object");
    }

    private void addSimpleReturn(String rawReturnType) {
        ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(rawReturnType);
        codeBlock
                .addStatement("rs.next()")
                .addStatement("return $L(1)", resultSetType.getMethod());
    }

    private void setQueryParameters(String rawQuery) {
        long numberOfParams = rawQuery.chars().filter(it -> it == '?').count();
        if (numberOfParams > 0) {
            if (numberOfParams != parameters.size()) throw new CodegenException("The number of parameters informed is not the same as necessary in the query.");

            AtomicInteger index = new AtomicInteger(1);
            parameters.forEach((parameter) -> {
                if (!ResultSetTypeEnum.isNativeType(parameter.getType())) throw new CodegenException(String.format("Parameter type not supported: %s.", parameter.getType()));

                ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(parameter.getType());

                codeBlock
                        .addStatement("$L($L, $L)", resultSetType.setMethod(), index.getAndIncrement(), parameter.getName());
            });
        }
    }

    private String getPackageName(String rawObject) {
        return rawObject.replace(String.format(".%s", getClassName(rawObject)), "");
    }

    private String getClassName(String rawObject) {
        String[] split = rawObject.split("\\.");
        return split[split.length - 1];
    }
}
