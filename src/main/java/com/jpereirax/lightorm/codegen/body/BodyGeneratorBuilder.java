package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Map;

public class BodyGeneratorBuilder {

    private final BodyGenerator generator;

    public BodyGeneratorBuilder() {
        this.generator = new BodyGenerator();
    }

    public BodyGeneratorBuilder typeUtils(Types typeUtils) {
        this.generator.typeUtils = typeUtils;
        return this;
    }

    public BodyGeneratorBuilder query(Query query) {
        this.generator.query = query;
        return this;
    }

    public BodyGeneratorBuilder returnType(TypeMirror returnType) {
        this.generator.returnType = returnType;
        return this;
    }

    public BodyGeneratorBuilder parameters(Map<String, String> parameters) {
        this.generator.parameters = parameters;
        return this;
    }

    public BodyGeneratorBuilder returnElement(Element returnElement) {
        this.generator.returnElement = returnElement;
        return this;
    }

    public BodyGenerator build() {
        return this.generator;
    }
}
