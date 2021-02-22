package org.pavelkondrashov.sqljdbcschool.userinterface;

import java.util.Scanner;

public class ViewProvider {
    private final Scanner scanner;

    public ViewProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString() {
        return scanner.nextLine();
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public void print(String input) {
        System.out.println(input);
    }

    public void printError() {
        System.out.println("No such a command, please retry");
    }
}
