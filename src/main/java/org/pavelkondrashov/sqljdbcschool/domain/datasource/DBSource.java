package org.pavelkondrashov.sqljdbcschool.domain.datasource;

import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Group;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.List;
import java.util.Objects;

public class DBSource {
    private final List<Group> groups;
    private final List<Course> courses;
    private final List<Student> students;

    public DBSource(List<Student> students, List<Group> groups, List<Course> courses) {
        this.groups = groups;
        this.courses = courses;
        this.students = students;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "DBSource: " +
                "groups = " + groups +
                ", courses = " + courses +
                ", students = " + students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DBSource dbSource = (DBSource) o;
        return Objects.equals(groups, dbSource.groups) &&
                Objects.equals(courses, dbSource.courses) &&
                Objects.equals(students, dbSource.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, courses, students);
    }
}
