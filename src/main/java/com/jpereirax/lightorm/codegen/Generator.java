package com.jpereirax.lightorm.codegen;

import sun.misc.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
