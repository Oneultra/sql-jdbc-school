package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class DBGenerator {
    private final SQLConnector sqlConnector;

    public DBGenerator(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    public void executeSqlScript(String filePath) {
        try(Connection connection = sqlConnector.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(getResourceAsReader(filePath));
        } catch (SQLException e) {
            throw new DBException("Failed to create a table", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("File at this path not exist: " + filePath, e);
        }
    }
}
