package com.jpereirax.lightorm.codegen.method;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class MethodGeneratorBuilder {

    private final MethodGenerator generator;

    public MethodGeneratorBuilder() {
        this.generator = new MethodGenerator();
    }

    public MethodGeneratorBuilder element(Element element) {
        this.generator.element = element;
        return this;
    }

    public MethodGeneratorBuilder typeUtils(Types typeUtils) {
        this.generator.typeUtils = typeUtils;
        return this;
    }

    public MethodGeneratorBuilder elementUtils(Elements elementUtils) {
        this.generator.elementUtils = elementUtils;
        return this;
    }

    public MethodGenerator build() {
        return this.generator;
    }
}
