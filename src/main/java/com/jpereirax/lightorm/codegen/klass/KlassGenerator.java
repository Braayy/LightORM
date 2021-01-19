package com.jpereirax.lightorm.codegen.klass;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.codegen.method.MethodGenerator;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class KlassGenerator implements Generator {

    protected Types typeUtils;
    protected Elements elementUtils;

    protected String packageName;
    protected String className;
    protected Element element;

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

    public static KlassGeneratorBuilder builder() {
        return new KlassGeneratorBuilder();
    }
}
