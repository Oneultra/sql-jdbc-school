package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentsDaoImplTest {
    private final SQLConnector connector = new SQLConnector("src/test/resources/database.properties");
    private final DBGenerator dbGenerator = new DBGenerator(connector);
    private final CoursesDao coursesDao = new CourseDaoImpl(connector);
    private final StudentsDao studentsDao = new StudentsDaoImpl(connector);

    @BeforeEach
    void createTable() {
        dbGenerator.executeSqlScript("school.sql");
    }

    @Test
    void saveShouldSaveStudentIntoDataBase() {
        Student expected = Student.builder()
                .withStudentId(5).withGroupId(2).withStudentName("Elon").withStudentSurname("Musk>")
                .build();

        studentsDao.save(expected);
        Student actual = studentsDao.findById(5).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveShouldThrowsExceptionWhenGetWrongParameters() {
        Student expected = Student.builder()
                .withStudentId(1).withGroupId(5).withStudentName("").withStudentSurname("")
                .build();
        assertThrows(DBException.class, () -> studentsDao.save(expected));
    }

    @Test
    void saveAllShouldSaveStudentsWhenGetListOfStudents() {
        List<Student> students = new ArrayList<>();
        Student student1 = Student.builder()
                .withStudentId(5).withGroupId(2).withStudentName("Elon").withStudentSurname("Musk")
                .build();

        Student student2 = Student.builder()
                .withStudentId(6).withGroupId(1).withStudentName("QWERT").withStudentSurname("QWe")
                .build();

        students.add(student1);
        students.add(student2);

        studentsDao.saveAll(students);
        List<Student> actual = studentsDao.findAll();
        assertThat(actual).contains(student1).contains(student2);
    }

    @Test
    void saveAllShouldThrowsExceptionWhenGetWrongListOfStudents() {
        List<Student> students = new ArrayList<>();
        Student student1 = Student.builder()
                .withStudentId(7).withGroupId(5).withStudentName("").withStudentSurname("")
                .build();

        Student student2 = Student.builder()
                .withStudentId(9).withGroupId(6).withStudentName(null).withStudentSurname("")
                .build();

        students.add(student1);
        students.add(student2);

        assertThrows(DBException.class, () -> studentsDao.saveAll(students));
    }

    @Test
    void findByIdShouldReturnStudentWithDesiredId() {
        Student expected = Student.builder()
                .withStudentId(1).withGroupId(2).withStudentName("Mikhail").withStudentSurname("Boyarskiy")
                .build();
        Student actual = studentsDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdShouldThrowsSQLExceptionWhenReceiveWrongConnection() throws SQLException {
        SQLConnector mockedConnector = mock(SQLConnector.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedConnector.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        StudentsDao studentsDao = new StudentsDaoImpl(mockedConnector);

        assertThrows(DBException.class, () -> studentsDao.findById(1));
    }

    @Test
    void findAllShouldReturnListOfStudentsWhenGetCorrectParameters() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentsDao.findById(1).orElse(null));
        expected.add(studentsDao.findById(2).orElse(null));

        List<Student> actual = studentsDao.findAll(1, 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllPaginationShouldThrowsSQLExceptionWhenReceiveWrongConnection() throws SQLException {
        SQLConnector mockedConnector = mock(SQLConnector.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedConnector.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        StudentsDao studentsDao = new StudentsDaoImpl(mockedConnector);

        assertThrows(DBException.class, () -> studentsDao.findAll(1, 1));
    }

    @Test
    void findAllShouldThrowsSQLExceptionWhenReceiveWrongConnection() throws SQLException {
        SQLConnector mockedConnector = mock(SQLConnector.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedConnector.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        StudentsDao studentsDao = new StudentsDaoImpl(mockedConnector);

        assertThrows(DBException.class, studentsDao::findAll);
    }

    @Test
    void findAllShouldReturnListOfAllStudentsWhenGetWrongParameters() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentsDao.findById(1).orElse(null));
        expected.add(studentsDao.findById(2).orElse(null));

        List<Student> actual = studentsDao.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteShouldDeleteStudentFromDataBaseWhenGetStudentID() {
        studentsDao.deleteById(1);

        Student verifiable = studentsDao.findById(1).orElse(null);
        assertThat(verifiable).isNull();
    }

    @Test
    void updateShouldUpdateStudentWhenGetStudent() {
        Student expected = Student.builder()
                .withStudentId(1).withGroupId(1).withStudentName("Name").withStudentSurname("Surname")
                .build();

        studentsDao.update(expected);
        Student actual = studentsDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldThrowExceptionWhenGetNullStudentName() {
        Student student = Student.builder()
                .withStudentId(1).withGroupId(1).withStudentName(null).withStudentSurname("Surname")
                .build();
        assertThrows(DBException.class, () -> studentsDao.update(student));
    }

    @Test
    void findAllRelatedToCourseByNameShouldReturnListOfStudentsRelatedToCourseName() {
        List<Student> expected = new ArrayList<>();
        expected.add(studentsDao.findById(1).orElse(null));
        expected.add(studentsDao.findById(2).orElse(null));

        List<Student> actual = studentsDao.findAllByCourseName("Music");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void assignStudentToCourseShouldAddRecordToTableStudentsToCourses() {
        Course expected = Course.builder()
                .withCourseId(2).withCourseName("Music").withCourseDescription("blablabla")
                .build();

        studentsDao.addToCourse(1, 2);
        List<Course> actual = coursesDao.findAllByStudentId(1);
        assertThat(actual).contains(expected);
    }

    @Test
    void addToCourseShouldThrowExceptionWhenGetWrongParameters() {
        assertThatThrownBy(() -> studentsDao.addToCourse(3, 3))
                .isInstanceOf(DBException.class).hasMessage("Added student to course Failed");
    }
}
