package org.pavelkondrashov.sqljdbcschool;

import org.pavelkondrashov.sqljdbcschool.service.ApplicationContextInjector;
import org.pavelkondrashov.sqljdbcschool.service.Controller;
import org.pavelkondrashov.sqljdbcschool.service.command.CommandProvider;

public class SqlSchoolApplication {
    public static void main(String[] args) {
        ApplicationContextInjector contextInjector = ApplicationContextInjector.getInstance();
        Controller controller = contextInjector.getController();
        CommandProvider commandProvider = contextInjector.getCommandProvider();
        controller.load();
        commandProvider.run();
    }
}
