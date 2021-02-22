package org.pavelkondrashov.sqljdbcschool.dao;

import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.List;

public interface StudentsDao extends CrudDao<Student, Integer> {
    List<Student> findAllByCourseName(String courseName);

    void addToCourse(Integer studentId, Integer courseId);
}
