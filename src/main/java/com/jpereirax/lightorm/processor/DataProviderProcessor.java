package com.jpereirax.lightorm.processor;

import com.jpereirax.lightorm.annotation.DataProvider;
import com.jpereirax.lightorm.codegen.Generator;
import com.jpereirax.lightorm.codegen.klass.KlassGenerator;
import com.jpereirax.lightorm.exception.CodegenException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({ "com.jpereirax.lightorm.annotation.DataProvider" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataProviderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations.isEmpty()) return false;

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(DataProvider.class);

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error("The annotation @DataProvider can only be applied on interfaces.", element);
                continue;
            }

            generateClass(element);
        }

        return false;
    }

    private void generateClass(Element element) {
        try {
            String packageName = packageName(element);
            String className = className(element);

            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(String.format("%s.%s", packageName, className));
            Writer writer = sourceFile.openWriter();

            Generator generator = KlassGenerator.builder()
                    .packageName(packageName)
                    .className(className)
                    .element(element)
                    .build();

            writer.write(generator.generate());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String packageName(Element element) {
        List<PackageElement> packageElements = ElementFilter.packagesIn(Collections.singletonList(element.getEnclosingElement()));
        if (packageElements.isEmpty()) throw new CodegenException("Failed to generate class: Package name not found.");

        PackageElement packageElement = packageElements.get(0);
        return String.format("%s.impl", packageElement.getQualifiedName().toString());
    }

    private String className(Element element) {
        return String.format("%sImpl", element.getSimpleName().toString());
    }

    private void error(String message, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
