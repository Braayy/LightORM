package com.jpereirax.lightorm.codegen;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import lombok.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@Builder
public class KlassGenerator implements Generator {

    private final Types typeUtils;
    private final Elements elementUtils;

    private final String packageName;
    private final String className;
    private final Element element;

    @Override
    public String generate() {
        String template = template("klass");

        String superClass = className.replace("Impl", "");
        String superClassPackage = packageName.replace("impl", superClass);

        template = template
                .replace("{packageName}", packageName)
                .replace("{superClassPackage}", superClassPackage)
                .replace("{className}", className)
                .replace("{superClass}", superClass);

        Generator methodGenerator = MethodGenerator.builder()
                .element(element)
                .typeUtils(typeUtils)
                .build();
        template = template.replace("{methods}", methodGenerator.generate());

        CompilationUnit compilationUnit = StaticJavaParser.parse(template);
        return compilationUnit.toString();
    }
}
