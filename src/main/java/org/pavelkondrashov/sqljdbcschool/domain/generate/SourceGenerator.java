package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.pavelkondrashov.sqljdbcschool.domain.datasource.DBSource;
import org.pavelkondrashov.sqljdbcschool.domain.reader.FileReader;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Group;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.List;

public class SourceGenerator {
    private static final String STUDENTS_NAME_FILE_PATH = "src/main/resources/names.txt";
    private static final String STUDENTS_SURNAMES_FILE_PATH = "src/main/resources/lastnames.txt";
    private static final String COURSES_FILE_PATH = "src/main/resources/courses.txt";

    private static final int MAX_STUDENTS = 200;
    private static final int MAX_GROUPS = 10;
    private static final int MAX_ID_GROUP = 99;
    private static final int MAX_COURSES = 10;

    private final FileReader fileReader;
    private final CourseGenerator courseGenerator;
    private final GroupsGenerator groupsGenerator;
    private final StudentsGenerator studentsGenerator;

    public SourceGenerator(FileReader fileReader, CourseGenerator courseGenerator,
                           GroupsGenerator groupsGenerator, StudentsGenerator studentsGenerator) {
        this.fileReader = fileReader;
        this.courseGenerator = courseGenerator;
        this.groupsGenerator = groupsGenerator;
        this.studentsGenerator = studentsGenerator;
    }

    public DBSource generateDBSource() {
        List<String> studentsNames = fileReader.readFile(STUDENTS_NAME_FILE_PATH);
        List<String> studentsSurnames = fileReader.readFile(STUDENTS_SURNAMES_FILE_PATH);
        List<String> courseTitleAndDescription = fileReader.readFile(COURSES_FILE_PATH);
        List<Group> groups = groupsGenerator.generateGroups(MAX_ID_GROUP, MAX_GROUPS);
        List<Course> courses = courseGenerator.generateCourses(MAX_COURSES, courseTitleAndDescription);
        List<Student> students = studentsGenerator.generateStudents(studentsNames, studentsSurnames,
                MAX_STUDENTS, MAX_GROUPS);

        return new DBSource(students, groups, courses);
    }
}
