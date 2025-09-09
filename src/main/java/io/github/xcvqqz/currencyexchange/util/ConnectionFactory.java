package io.github.xcvqqz.currencyexchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.xcvqqz.currencyexchange.exception.DataBaseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties properties;
    private static final HikariDataSource dataSource;
    private static final HikariConfig config;
    private static final String DB_PATH_CONFIGURATION = "configurations.properties";
    private static final String DB_ERROR = "Database error: failed to establish connection to the database";

    static {
        try {
            properties = new Properties();
            properties.load(ConnectionFactory.class.getClassLoader()
                    .getResourceAsStream(DB_PATH_CONFIGURATION));

            config = new HikariConfig(properties);
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new DataBaseException(DB_ERROR);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}