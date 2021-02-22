package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.pavelkondrashov.sqljdbcschool.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseGenerator {
    private static final int PLACE_OF_NAME = 0;
    private static final int PLACE_OF_DESCRIPTION = 1;
    private static final String SPLIT_SYMBOL = "_";

    public List<Course> generateCourses(int maxCourses, List<String> courseNamesAndDescription) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < maxCourses; i++) {
            String line = courseNamesAndDescription.get(i);
            courses.add(Course.builder()
                    .withCourseId(i + 1)
                    .withCourseName(lineSplitter(PLACE_OF_NAME, line))
                    .withCourseDescription(lineSplitter(PLACE_OF_DESCRIPTION, line))
                    .build());
        }
        return courses;
    }

    private String lineSplitter(int targetPoint, String line) {
        return line.split(SPLIT_SYMBOL)[targetPoint];
    }
}
