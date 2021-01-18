package com.jpereirax.lightorm.codegen.body;

import com.jpereirax.lightorm.annotation.Query;

public class BodyGeneratorBuilder {

    private final BodyGenerator generator;

    public BodyGeneratorBuilder() {
        this.generator = new BodyGenerator();
    }

    public BodyGeneratorBuilder query(Query query) {
        this.generator.query = query;
        return this;
    }

    public BodyGenerator build() {
        return this.generator;
    }
}
