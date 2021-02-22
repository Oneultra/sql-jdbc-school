package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DBGeneratorTest {
    @InjectMocks
    private DBGenerator dbGenerator;

    @Mock
    private SQLConnector mockedConnector;

    @Test
    void executeSqlScriptShouldThrowExceptionWhenGetWrongFilePath() {
        String filePath = "fws";
        assertThatThrownBy(() -> dbGenerator.executeSqlScript(filePath)).isInstanceOf(RuntimeException.class);
    }
}
