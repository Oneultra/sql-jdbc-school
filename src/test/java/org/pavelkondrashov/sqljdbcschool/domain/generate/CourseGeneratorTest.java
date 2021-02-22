package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.junit.jupiter.api.Test;
import org.pavelkondrashov.sqljdbcschool.entity.Course;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseGeneratorTest {
    private final CourseGenerator courseGenerator = new CourseGenerator();

    @Test
    void generateCourseShouldReturnListOfCoursesWhenGetAllParameters() {
        List<String> courses = new ArrayList<>();

        courses.add("course one_descr one");
        courses.add("course two_descr two");
        courses.add("course three_descr three");

        List<Course> expected = new ArrayList<>();

        expected.add(Course.builder().withCourseId(1)
                .withCourseName("course one")
                .withCourseDescription("descr one").build());
        expected.add(Course.builder().withCourseId(2)
                .withCourseName("course two")
                .withCourseDescription("descr two").build());
        expected.add(Course.builder().withCourseId(3)
                .withCourseName("course three")
                .withCourseDescription("descr three").build());

        List<Course> actual = courseGenerator.generateCourses(3, courses);

        assertThat(expected).isEqualTo(actual);
    }
}
