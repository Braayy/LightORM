package com.jpereirax.lightorm.codegen;

import com.jpereirax.lightorm.core.datasource.DataSource;
import com.jpereirax.lightorm.core.generator.Generator;
import com.squareup.javapoet.*;
import lombok.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.sql.Connection;
import java.util.List;

@Builder
public class KlassGenerator implements Generator<TypeSpec> {

    private final ProcessingEnvironment processingEnvironment;

    private final String packageName;
    private final String className;
    private final Element element;

    @Override
    public TypeSpec generate() {
        String superClass = className.replace("Impl", "");
        String superClassPackage = packageName.replace(".impl", "");

        FieldSpec connectionField = FieldSpec
                .builder(Connection.class, "connection")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer(
                        CodeBlock
                                .builder()
                                .add("$T.getInstance().getConnection()", DataSource.class)
                                .build()
                )
                .build();

        Generator<List<MethodSpec>> methods = MethodGenerator.builder()
                .processingEnvironment(processingEnvironment)
                .element(element)
                .build();

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(superClassPackage, superClass))
                .addField(connectionField)

                .addMethods(methods.generate())

                .build();
    }
}
