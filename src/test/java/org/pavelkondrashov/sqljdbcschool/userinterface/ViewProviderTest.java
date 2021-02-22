package org.pavelkondrashov.sqljdbcschool.userinterface;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewProviderTest {
    @InjectMocks
    private ViewProvider viewProvider;

    @Mock
    private Scanner scanner;

    @Test
    void readStringShouldReturnStringWithInput() {
        String expected = "qwerty";
        when(scanner.nextLine()).thenReturn("qwerty");

        String actual = viewProvider.readString();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readIntShouldReturnIntegerWithInput() {
        int expected = 101101;
        when(scanner.nextInt()).thenReturn(101101);

        int actual = viewProvider.readInt();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void printShouldReturnStringWithMessageGetFromConsole() throws Exception {
        String actual = "qwerty";

        String expected = tapSystemOut(() -> {
            viewProvider.print(actual);
        });
        assertThat(actual).isEqualTo(expected.trim());
    }

    @Test
    void printErrorShouldPrintErrorMessage() throws Exception {
        String actual = "No such a command, please retry";
        String expected = tapSystemOut(() -> {
            viewProvider.printError();
        });
        assertThat(actual).isEqualTo(expected.trim());
    }
}
