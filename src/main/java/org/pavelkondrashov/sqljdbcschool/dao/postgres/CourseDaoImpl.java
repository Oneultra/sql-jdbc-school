package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;
import org.pavelkondrashov.sqljdbcschool.entity.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CoursesDao {
    private static final String SAVE_QUERY =
            "INSERT INTO school.courses(course_id, course_name, description) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.courses WHERE course_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.courses ORDER BY course_id";
    private static final String FIND_ALL_PAGINATION_QUERY =
            "SELECT * FROM school.courses ORDER BY course_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school.courses SET course_name = ? , description = ?" +
            " WHERE course_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.courses WHERE course_id=?";

    private static final String FIND_ALL_STUDENT_COURSES_QUERY = "SELECT * FROM school.courses WHERE course_id IN" +
            "(SELECT course_id FROM school.students_courses WHERE student_id = ?)";
    private static final String DELETE_STUDENT_FROM_ONE_COURSE_QUERY = "DELETE FROM school.students_courses " +
            "WHERE student_id = ? AND course_id = ?";

    public CourseDaoImpl(SQLConnector connector) {
        super (connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Course> findAllByStudentId(int studentId) {
        return findAllByIntParameter(studentId, FIND_ALL_STUDENT_COURSES_QUERY);
    }

    @Override
    public void removeStudentFromCourse(Integer studentId, Integer courseId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     DELETE_STUDENT_FROM_ONE_COURSE_QUERY)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Can`t remove student with id: " + studentId +
                    " from course with id:" + courseId, e);
        }
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Course.builder()
                .withCourseId(resultSet.getInt("course_id"))
                .withCourseName(resultSet.getString("course_name"))
                .withCourseDescription(resultSet.getString("description"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Course course) throws SQLException {
        preparedStatement.setInt(1, course.getCourseId());
        preparedStatement.setString(2, course.getCourseName());
        preparedStatement.setString(3, course.getCourseDescription());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Course course) throws SQLException {
        preparedStatement.setString(1, course.getCourseName());
        preparedStatement.setString(2, course.getCourseDescription());
        preparedStatement.setInt(3, course.getCourseId());
    }
}
