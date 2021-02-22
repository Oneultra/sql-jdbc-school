package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentsDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentsDao {
    private static final String SAVE_QUERY = "INSERT INTO school.students VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.students ORDER BY student_id";
    private static final String FIND_ALL_PAGINATION_QUERY =
            "SELECT * FROM school.students ORDER BY student_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school.students SET group_id = ?, student_name=?, " +
            "student_last_name=? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.students WHERE student_id=?";

    private static final String FIND_ALL_RELATED_TO_COURSE_NAME =
            "SELECT * FROM school.students WHERE student_id IN " +
                    "(SELECT student_id FROM school.students_courses WHERE course_id IN" +
                    "(SELECT course_id FROM school.courses WHERE course_name = ?)) " +
                    "ORDER BY student_id";
    private static final String ADD_STUDENT_TO_COURSE_QUERY = "INSERT into school.students_courses VALUES (?,?)";

    private static final String FAILED = "Failed";
    private static final String ASSIGNING_MESSAGE = "Added student to course ";

    public StudentsDaoImpl(SQLConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return findAllByStringParam(courseName, FIND_ALL_RELATED_TO_COURSE_NAME);
    }

    @Override
    public void addToCourse(Integer studentId, Integer courseId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_STUDENT_TO_COURSE_QUERY)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(ASSIGNING_MESSAGE + FAILED, e);
        }
    }

    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Student.builder()
                .withStudentId(resultSet.getInt("student_id"))
                .withGroupId(resultSet.getInt("group_id"))
                .withStudentName(resultSet.getString("student_name"))
                .withStudentSurname(resultSet.getString("student_last_name"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Student student) throws SQLException {
        preparedStatement.setInt(1, student.getStudentId());
        preparedStatement.setInt(2, student.getGroupId());
        preparedStatement.setString(3, student.getStudentName());
        preparedStatement.setString(4, student.getStudentSurname());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Student student) throws SQLException {
        preparedStatement.setInt(1, student.getGroupId());
        preparedStatement.setString(2, student.getStudentName());
        preparedStatement.setString(3, student.getStudentSurname());
        preparedStatement.setInt(4, student.getStudentId());
    }
}
