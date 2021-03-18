package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.pavelkondrashov.sqljdbcschool.dao.CrudDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, Integer> {
    private static final String MESSAGE_FORMAT = "%s%s";
    private static final String SAVING_FROM_LIST_MESSAGE = "Saving from list";
    private static final String SAVE_MESSAGE = "Saving";
    private static final String FIND_ALL_MESSAGE = "Find all";
    private static final String FIND_BY_ID_MESSAGE = "Find by id";
    private static final String DELETE_MESSAGE = "Deleting by id";
    private static final String UPDATE_MESSAGE = "Updating";
    private static final String FAILED_MESSAGE = "Failed";

    private static final BiConsumer<PreparedStatement, Integer> INTEGER_CONSUMER
            = (PreparedStatement preparedStatement, Integer param) -> {
        try {
            preparedStatement.setInt(1, param);
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, "Integer consumer", FAILED_MESSAGE), e);
        }
    };

    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER
            = (PreparedStatement preparedStatement, String param) -> {
        try {
            preparedStatement.setString(1, param);
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, "String consumer", FAILED_MESSAGE), e);
        }
    };

    protected final SQLConnector connector;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findAllPaginationQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    protected AbstractCrudDaoImpl(SQLConnector connector, String saveQuery, String findByIdQuery,
                               String findAllQuery, String findAllPaginationQuery, String updateQuery,
                               String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findAllPaginationQuery = findAllPaginationQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        try (Connection connection = connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            insert(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, SAVE_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            for (E entity : entities) {
                insert(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, SAVING_FROM_LIST_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public Optional<E> findById(Integer id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, FIND_BY_ID_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public void update(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            updateValues(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, UPDATE_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public List<E> findAll(int page, int itemPerPage) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllPaginationQuery)) {
            preparedStatement.setInt(1, itemPerPage);
            preparedStatement.setInt(2, ((itemPerPage * page) - itemPerPage));
            List<E> entities = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, FIND_ALL_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public List<E> findAll() {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, FIND_ALL_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, DELETE_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    protected List<E> findAllByIntParameter(Integer parameter, String query) {
        return findAllByParameter(parameter, query, INTEGER_CONSUMER);
    }

    protected List<E> findAllByStringParam(String parameter, String query) {
        return findAllByParameter(parameter, query, STRING_CONSUMER);
    }

    protected <P> List<E> findAllByParameter(P parameter, String query,
                                             BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<E> entities = new ArrayList<>();
            consumer.accept(preparedStatement, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBException(String.format(MESSAGE_FORMAT, FIND_ALL_MESSAGE, FAILED_MESSAGE), e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;
}
