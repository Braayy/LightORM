package com.jpereirax.lightorm.codegen;

import com.jpereirax.lightorm.core.annotation.Query;
import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class CodeBlockGeneratorTest {

    private CodeBlockGenerator.CodeBlockGeneratorBuilder builder;
    private CodeBlockGenerator generator;

    @BeforeEach
    void setUp() {
        ProcessingEnvironment processingEnvironment = mock(ProcessingEnvironment.class);

        TypeMirror returnType = mock(TypeMirror.class);
        when(returnType.toString()).thenReturn("java.lang.String");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("java.lang.String", "name");

        builder = CodeBlockGenerator.builder()
                .processingEnvironment(processingEnvironment)
                .returnType(returnType)
                .parameters(parameters);
    }

    @Test
    void whenExecuteSimpleQueryShouldReturnOK() {
        Query query = mock(Query.class);
        when(query.value()).thenReturn("SELECT LEVEL FROM USER WHERE NAME = ?");

        generator = builder.query(query).build();

        CodeBlock codeBlock = generator.generate();

        assertNotNull(codeBlock);
    }
}