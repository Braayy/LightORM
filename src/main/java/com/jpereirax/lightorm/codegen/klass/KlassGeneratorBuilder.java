package com.jpereirax.lightorm.codegen.klass;

import javax.lang.model.element.Element;

public class KlassGeneratorBuilder {

    private final KlassGenerator generator;

    public KlassGeneratorBuilder() {
        this.generator = new KlassGenerator();
    }

    public KlassGeneratorBuilder packageName(String packageName) {
        this.generator.packageName = packageName;
        return this;
    }

    public KlassGeneratorBuilder className(String className) {
        this.generator.className = className;
        return this;
    }

    public KlassGeneratorBuilder element(Element element) {
        this.generator.element = element;
        return this;
    }

    public KlassGenerator build() {
        return this.generator;
    }
}
