package org.pavelkondrashov.sqljdbcschool.service;

import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.dao.postgres.CourseDaoImpl;
import org.pavelkondrashov.sqljdbcschool.dao.postgres.GroupsDaoImpl;
import org.pavelkondrashov.sqljdbcschool.dao.postgres.StudentsDaoImpl;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.datasource.DBSource;
import org.pavelkondrashov.sqljdbcschool.domain.generate.CourseGenerator;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.domain.generate.GroupsGenerator;
import org.pavelkondrashov.sqljdbcschool.domain.generate.SourceGenerator;
import org.pavelkondrashov.sqljdbcschool.domain.generate.StudentsGenerator;
import org.pavelkondrashov.sqljdbcschool.domain.reader.FileReader;
import org.pavelkondrashov.sqljdbcschool.service.command.CommandProvider;
import org.pavelkondrashov.sqljdbcschool.userinterface.ViewProvider;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class ApplicationContextInjector {
    private static final String CONNECTION_FILEPATH = "src/main/resources/database.properties";

    private static final Random RANDOM = new SecureRandom();
    private static final FileReader FILE_READER = new FileReader();

    private static final CourseGenerator COURSE_GENERATOR = new CourseGenerator();
    private static final GroupsGenerator GROUPS_GENERATOR = new GroupsGenerator(RANDOM);
    private static final StudentsGenerator STUDENTS_GENERATOR = new StudentsGenerator(RANDOM);
    private static final SourceGenerator DATA_SOURCE_CREATOR = new SourceGenerator(FILE_READER, COURSE_GENERATOR,
            GROUPS_GENERATOR, STUDENTS_GENERATOR);
    private static final DBSource DATA_BASE_SOURCE = DATA_SOURCE_CREATOR.generateDBSource();

    private static final SQLConnector CONNECTOR = new SQLConnector(CONNECTION_FILEPATH);
    private static final DBGenerator DATA_BASE_GENERATOR = new DBGenerator(CONNECTOR);
    private static final GroupsDao GROUP_DAO = new GroupsDaoImpl(CONNECTOR);
    private static final CoursesDao COURSE_DAO = new CourseDaoImpl(CONNECTOR);
    private static final StudentsDao STUDENT_DAO = new StudentsDaoImpl(CONNECTOR);

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ViewProvider VIEW_PROVIDER = new ViewProvider(SCANNER);
    private static final CommandProvider COMMAND_PROVIDER = new CommandProvider(COURSE_DAO, GROUP_DAO,
            STUDENT_DAO, VIEW_PROVIDER);

    private static final Controller CONTROLLER = new Controller(DATA_BASE_GENERATOR, DATA_BASE_SOURCE,
            COURSE_DAO, GROUP_DAO, STUDENT_DAO, VIEW_PROVIDER);

    private static volatile ApplicationContextInjector contextInjector;

    private ApplicationContextInjector() {

    }

    public static ApplicationContextInjector getInstance() {
        if (contextInjector == null) {
            synchronized (ApplicationContextInjector.class) {
                if (contextInjector == null) {
                    contextInjector = new ApplicationContextInjector();
                }
            }
        }
        return contextInjector;
    }

    public Controller getController() {
        return CONTROLLER;
    }

    public CommandProvider getCommandProvider() {
        return COMMAND_PROVIDER;
    }
}
