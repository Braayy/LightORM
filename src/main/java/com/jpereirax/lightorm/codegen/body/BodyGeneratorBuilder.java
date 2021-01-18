package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;

import java.util.Map;

public class BodyGeneratorBuilder {

    private final BodyGenerator generator;

    public BodyGeneratorBuilder() {
        this.generator = new BodyGenerator();
    }

    public BodyGeneratorBuilder query(Query query) {
        this.generator.query = query;
        return this;
    }

    public BodyGeneratorBuilder returnType(String returnType) {
        this.generator.returnType = returnType;
        return this;
    }

    public BodyGeneratorBuilder parameters(Map<String, String> parameters) {
        this.generator.parameters = parameters;
        return this;
    }

    public BodyGenerator build() {
        return this.generator;
    }
}
