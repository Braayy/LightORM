package com.jpereirax.lightorm.type;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParameterType {

    private final String type;
    private final String name;
}
