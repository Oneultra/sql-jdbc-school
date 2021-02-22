package org.pavelkondrashov.sqljdbcschool.domain.reader;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileReaderTest {
    private FileReader fileReader = new FileReader();

    @Test
    void readFileShouldReturnListOfStringsWhenGetCorrectFilePath() {
        String filePath = "src/test/resources/courses.txt";

        List<String> expected = new ArrayList<>();
        expected.add("Literature_blabla");
        expected.add("Music_blablabla");

        List<String> actual = fileReader.readFile(filePath);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void readFileShouldThrowIllegalArgumentExceptionWhenGetWrongFilePath() {
        String filePath = "Wrong file path";

        assertThatThrownBy(() -> fileReader.readFile(filePath)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File at this path not exist: " + filePath);
    }
}
