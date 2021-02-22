package org.pavelkondrashov.sqljdbcschool.service;

import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.domain.datasource.DBSource;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.userinterface.ViewProvider;

import static java.lang.System.lineSeparator;

public class Controller {
    private static final String NEW_LINE = "\n";
    private static final String MENU_SEPARATOR = "-".repeat(54);

    private static final String MAIN_MENU_TEXT = "Application main menu." + lineSeparator() + MENU_SEPARATOR
            + lineSeparator() + "1. Find all groups with less or equals student count" + lineSeparator()
            + "2. Find all students related to course with given name" + lineSeparator() + "3. Add new student"
            + lineSeparator() + "4. Delete student by STUDENT_ID" + lineSeparator()
            + "5. Add a student to the course (from a list)" + lineSeparator()
            + "6. Remove the student from one of his or her courses" + lineSeparator() + MENU_SEPARATOR
            + lineSeparator() + "Please enter sub-menu number, or 0 to EXIT application:";

    public static final String FILE_NAME = "school.sql";

    private final DBGenerator dbGenerator;
    private final DBSource dbSource;
    private final CoursesDao coursesDao;
    private final GroupsDao groupsDao;
    private final StudentsDao studentsDao;
    private final ViewProvider viewProvider;

    public Controller(DBGenerator dbGenerator, DBSource dbSource,
                      CoursesDao coursesDao, GroupsDao groupsDao, StudentsDao studentsDao,
                      ViewProvider viewProvider) {
        this.dbGenerator = dbGenerator;
        this.dbSource = dbSource;
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
        this.studentsDao = studentsDao;
        this.viewProvider = viewProvider;
    }

    public void load() {
        dbGenerator.executeSqlScript(FILE_NAME);

        groupsDao.saveAll(dbSource.getGroups());
        studentsDao.saveAll(dbSource.getStudents());
        coursesDao.saveAll(dbSource.getCourses());

        String menu = String.join(NEW_LINE, MAIN_MENU_TEXT);
        viewProvider.print(menu);
    }
}
