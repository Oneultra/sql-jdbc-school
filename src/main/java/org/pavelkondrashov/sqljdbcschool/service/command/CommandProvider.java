package org.pavelkondrashov.sqljdbcschool.service.command;

import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Group;
import org.pavelkondrashov.sqljdbcschool.entity.Student;
import org.pavelkondrashov.sqljdbcschool.userinterface.ViewProvider;

import java.util.stream.Collectors;

public class CommandProvider {
    private static final int FIND_GROUPS_WITH_STUDENTS_COUNT = 1;
    private static final int FIND_STUDENTS_TO_COURSE_NAME = 2;
    private static final int ADD_NEW_STUDENT = 3;
    private static final int DELETE_STUDENT_BY_ID = 4;
    private static final int ADD_STUDENT_TO_COURSE = 5;
    private static final int REMOVE_STUDENT_FROM_COURSE = 6;
    private static final int EXIT = 0;

    private static final String ENTER_STUDENT_ID = "Enter student id: ";
    private static final String COMPLETE_MESSAGE = "Complete";

    private static final String NEW_LINE = "\n";

    private final CoursesDao coursesDao;
    private final GroupsDao groupsDao;
    private final StudentsDao studentsDao;
    private final ViewProvider viewProvider;

    public CommandProvider(CoursesDao coursesDao, GroupsDao groupsDao,
                           StudentsDao studentsDao, ViewProvider viewProvider) {
        this.coursesDao = coursesDao;
        this.groupsDao = groupsDao;
        this.studentsDao = studentsDao;
        this.viewProvider = viewProvider;
    }

    public void run() {
        boolean keepGoing = true;
        int menuItem;
        while (keepGoing) {
            viewProvider.print("Enter a command: ");
            menuItem = viewProvider.readInt();

            switch (menuItem) {
                case FIND_GROUPS_WITH_STUDENTS_COUNT:
                    findGroupsWithStudentCount();
                    break;
                case FIND_STUDENTS_TO_COURSE_NAME:
                    findAllStudentsToCourseName();
                    break;
                case ADD_NEW_STUDENT:
                    addNewStudent();
                    break;
                case DELETE_STUDENT_BY_ID:
                    deleteStudentById();
                    break;
                case ADD_STUDENT_TO_COURSE:
                    addStudentToCourse();
                    break;
                case REMOVE_STUDENT_FROM_COURSE:
                    removeStudentFromCourse();
                    break;
                case EXIT:
                    exit();
                    keepGoing = false;
                    break;
                default:
                    viewProvider.printError();
            }
        }
    }

    public void findGroupsWithStudentCount() {
        viewProvider.print("Enter count: ");
        int count = viewProvider.readInt();
        viewProvider.print(groupsDao.findAllByStudentsNumber(count).stream()
                .map(Group::toString)
                .collect(Collectors.joining(NEW_LINE)));
    }

    public void findAllStudentsToCourseName() {
        viewProvider.print(coursesDao.findAll().stream()
                .map(Course::getCourseName)
                .collect(Collectors.joining(NEW_LINE)));
        viewProvider.print("Enter a course name: ");
        String courseName = viewProvider.readString();
        viewProvider.print(studentsDao.findAllByCourseName(courseName).stream()
                .map(Student::toString)
                .collect(Collectors.joining(NEW_LINE)));
    }

    public void addNewStudent() {
        viewProvider.print(ENTER_STUDENT_ID);
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter a group id: ");
        int groupId = viewProvider.readInt();
        viewProvider.print("Enter a student name: ");
        String studentName = viewProvider.readString();
        viewProvider.print("Enter a student last name: ");
        String studentSurname = viewProvider.readString();
        Student student = Student.builder()
                .withStudentId(studentId)
                .withGroupId(groupId)
                .withStudentName(studentName)
                .withStudentSurname(studentSurname)
                .build();
        studentsDao.save(student);
        viewProvider.print(COMPLETE_MESSAGE);
    }

    public void deleteStudentById() {
        viewProvider.print(ENTER_STUDENT_ID);
        int studentId = viewProvider.readInt();
        studentsDao.deleteById(studentId);
        viewProvider.print(COMPLETE_MESSAGE);
    }

    public void addStudentToCourse() {
        viewProvider.print(ENTER_STUDENT_ID);
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter a course id: ");
        int courseId = viewProvider.readInt();
        studentsDao.addToCourse(studentId, courseId);
        viewProvider.print(COMPLETE_MESSAGE);
    }

    public void removeStudentFromCourse() {
        viewProvider.print(ENTER_STUDENT_ID);
        int studentId = viewProvider.readInt();
        viewProvider.print("Enter course id: ");
        int courseId = viewProvider.readInt();
        coursesDao.removeStudentFromCourse(studentId, courseId);
        viewProvider.print(COMPLETE_MESSAGE);
    }

    public void exit() {
        viewProvider.print("Bye bye");
    }
}
