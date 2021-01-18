package com.jpereirax.lightorm.codegen.method;

import javax.lang.model.element.Element;

public class MethodGeneratorBuilder {

    private final MethodGenerator generator;

    public MethodGeneratorBuilder() {
        this.generator = new MethodGenerator();
    }

    public MethodGeneratorBuilder element(Element element) {
        this.generator.element = element;
        return this;
    }

    public MethodGenerator build() {
        return this.generator;
    }
}
