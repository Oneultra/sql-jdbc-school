package org.pavelkondrashov.sqljdbcschool.dao;

import org.pavelkondrashov.sqljdbcschool.entity.Course;

import java.util.List;

public interface CoursesDao extends CrudDao<Course, Integer> {
    List<Course> findAllByStudentId(int studentId);

    void removeStudentFromCourse(Integer studentId, Integer courseId);
}
