package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDaoImplTest {

    private final SQLConnector connector = new SQLConnector("src/test/resources/database.properties");
    private final DBGenerator dbGenerator = new DBGenerator(connector);
    private final CoursesDao coursesDao = new CourseDaoImpl(connector);
    private final StudentsDao studentsDao = new StudentsDaoImpl(connector);

    @BeforeEach
    void createTable() {
        dbGenerator.executeSqlScript("school.sql");
    }

    @Test
    void saveShouldSaveCourseToDataBaseWhenGetCourse() {
        Course expected = Course.builder()
                .withCourseId(3).withCourseName("expected name").withCourseDescription("expected description")
                .build();

        coursesDao.save(expected);
        Course actual = coursesDao.findById(3).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void saveAllShouldSaveCoursesWhenGetListOfCourses(){
        List<Course> coursesForSave = new ArrayList<>();
        Course course1 = Course.builder()
                .withCourseId(3).withCourseName("course two").withCourseDescription("course descr 2")
                .build();
        Course course2 = Course.builder()
                .withCourseId(5).withCourseName("course five").withCourseDescription("course descr 5")
                .build();

        coursesForSave.add(course1);
        coursesForSave.add(course2);

        coursesDao.saveAll(coursesForSave);

        List<Course> actual = coursesDao.findAll();
        assertThat(actual).contains(course1).contains(course2);

    }

    @Test
    void findByIdShouldReturnCourseWhenGetId() {
        Course expected = Course.builder()
                .withCourseId(1).withCourseName("Literature").withCourseDescription("blabla")
                .build();
        Course actual = coursesDao.findById(1).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfCoursesWhenGetCorrectParameters() {
        List<Course> expected = new ArrayList<>();
        expected.add(coursesDao.findById(1).orElse(null));
        expected.add(coursesDao.findById(2).orElse(null));

        List<Course> actual = coursesDao.findAll(1,2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfCoursesWhenGetWrongParameters() {
        List<Course> expected = new ArrayList<>();
        expected.add(coursesDao.findById(1).orElse(null));
        expected.add(coursesDao.findById(2).orElse(null));

        List<Course> actual = coursesDao.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteByIdShouldDeleteCourseWhenGetId() {
        coursesDao.deleteById(2);
        Course actual = coursesDao.findById(2).orElse(null);

        assertThat(actual).isNull();
    }
    
    @Test
    void updateShouldUpdateCourseWhenGetCourse() {
        Course expected = Course.builder()
                .withCourseId(2).withCourseName("Physics").withCourseDescription("physics decr")
                .build();

        coursesDao.update(expected);
        Course actual = coursesDao.findById(2).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllStudentCoursesShouldReturnListOfCoursesWhenGetStudentId() {
        List<Course> expected = new ArrayList<>();
        expected.add(coursesDao.findById(1).orElse(null));
        expected.add(coursesDao.findById(2).orElse(null));

        List<Course> actual = coursesDao.findAllByStudentId(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteStudentFromOneCourseShouldDeleteStudentFromTableStudentCourses() {
        Student student = Student.builder()
                .withStudentId(1).withGroupId(2).withStudentName("Mikhail").withStudentSurname("Boyarskiy")
                .build();

        coursesDao.removeStudentFromCourse(1, 2);
        assertThat(studentsDao.findAllByCourseName("Music")).isNotEmpty();
        assertThat(studentsDao.findAllByCourseName("Music")).doesNotContain(student);
    }
}
