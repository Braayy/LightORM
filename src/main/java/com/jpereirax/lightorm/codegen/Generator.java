package com.jpereirax.lightorm.codegen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface Generator {

    String generate();

    default String template(String name) {
        List<String> lines = new ArrayList<>();

        try {
            String fileName = String.format("/%s.template", name);
            InputStream inputStream = Generator.class.getResourceAsStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.join("\n", lines);
    }
}
