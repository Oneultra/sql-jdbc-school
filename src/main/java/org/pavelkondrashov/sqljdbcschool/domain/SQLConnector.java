package org.pavelkondrashov.sqljdbcschool.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.pavelkondrashov.sqljdbcschool.domain.exception.DBException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnector {
    private final HikariDataSource hikariDataSource;

    public SQLConnector(String fileConfigPath) {
        HikariConfig hikariConfig = new HikariConfig(fileConfigPath);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
