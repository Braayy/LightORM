package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.exception.CodegenException;
import com.jpereirax.lightorm.type.ResultSetTypeEnum;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jpereirax.lightorm.codegen.Constants.SINGLE_LINE_BREAK;

public class BodyGenerator implements Generator {

    protected Types typeUtils;

    protected Query query;
    protected TypeMirror returnType;
    protected Map<String, String> parameters;

    protected Element returnElement;

    @Override
    public String generate() {
        StringBuilder template = new StringBuilder(template("body").replace("{statementParams}", setQueryParameters()));
        String rawQuery = query.value().toUpperCase();

        String rawReturnType = returnType.toString();

        if (!rawReturnType.equals("void")) {
            if (!rawQuery.startsWith("SELECT")) throw new CodegenException("Only SELECT Query can return object.");

            if (ResultSetTypeEnum.isNativeType(rawReturnType)) {
                generateBasicReturnType(template);
            } else {
                generateComplexReturnType(template, rawReturnType.startsWith("java.util.List"));
            }
        }

        return template.toString().replace("{query}", rawQuery);
    }

    private void generateComplexReturnType(StringBuilder template, boolean isList) {
        List<ExecutableElement> executableElements = ElementFilter.constructorsIn(returnElement.getEnclosedElements());

        String returnObject = returnType.toString();
        if (isList) {
            DeclaredType declaredReturnType = (DeclaredType) returnType;
            TypeMirror returnObjectType = declaredReturnType.getTypeArguments().get(0);

            executableElements = ElementFilter.constructorsIn(typeUtils.asElement(returnObjectType).getEnclosedElements());

            returnObject = returnObjectType.toString();
            String listReturnType = "java.util.ArrayList<>";

            template
                    .append(String.format("%s response = new %s();", returnType, listReturnType))
                    .append(SINGLE_LINE_BREAK)
                    .append("while(rs.next()) {")
                    .append(SINGLE_LINE_BREAK);
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

            String method = resultSetType.getMethod().replace("?", String.valueOf(i + 1));

            parametersName.add(String.format("_%s", name));

            template
                    .append(String.format("%s _%s = new %s(%s);", defaultType, name, defaultType, method))
                    .append(SINGLE_LINE_BREAK);
        }

        String joinedParams = String.join(", ", parametersName);
        template
                .append(String.format("%s object = new %s(%s);", returnObject, returnObject, joinedParams))
                .append(SINGLE_LINE_BREAK);

        if (isList) {
            template
                    .append("response.add(object);")
                    .append(SINGLE_LINE_BREAK)
                    .append("}")
                    .append(SINGLE_LINE_BREAK);
        }
        template.append("return response;");
    }

    private void generateBasicReturnType(StringBuilder template) {
        ResultSetTypeEnum resultSetType = ResultSetTypeEnum.getResultSetTypeFromType(returnType.toString());
        template
                .append(String.format("return %s;", resultSetType.getMethod().replace("?", "1")));
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

    public static BodyGeneratorBuilder builder() {
        return new BodyGeneratorBuilder();
    }
}
