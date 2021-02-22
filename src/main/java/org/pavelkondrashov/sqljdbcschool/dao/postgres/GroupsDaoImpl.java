package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.entity.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupsDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupsDao {

    private static final String SAVE_QUERY = "INSERT INTO school.groups(group_id, group_name) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.groups ORDER BY group_id";
    private static final String FIND_ALL_PAGINATION_QUERY =
            "SELECT * FROM school.groups ORDER BY group_id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE school.groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.groups WHERE group_id=?";

    private static final String FIND_ALL_BY_STUDENT_COUNT_QUERY =
            "SELECT * FROM school.groups WHERE group_id IN " +
                    "(SELECT group_id FROM school.students GROUP BY group_id HAVING count(student_id)>=?);";

    public GroupsDaoImpl(SQLConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGINATION_QUERY,
                UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Group> findAllByStudentsNumber(int number) {
        return findAllByIntParameter(number, FIND_ALL_BY_STUDENT_COUNT_QUERY);
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Group group) throws SQLException {
        preparedStatement.setInt(1, group.getGroupId());
        preparedStatement.setString(2, group.getGroupName());
    }

    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Group.builder()
                .withGroupId(resultSet.getInt("group_id"))
                .withGroupName(resultSet.getString("group_name"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Group group) throws SQLException {
        preparedStatement.setString(1, group.getGroupName());
        preparedStatement.setInt(2, group.getGroupId());
    }
}
