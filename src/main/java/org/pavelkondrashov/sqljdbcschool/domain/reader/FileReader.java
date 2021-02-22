package org.pavelkondrashov.sqljdbcschool.domain.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    public List<String> readFile(String filePath) {
        try(Stream<String> txtData = Files.lines(Paths.get(filePath))) {
            return txtData.collect(Collectors.toList());
        } catch (IOException exception) {
            throw new IllegalArgumentException("File at this path not exist: " + filePath);
        }
    }
}
