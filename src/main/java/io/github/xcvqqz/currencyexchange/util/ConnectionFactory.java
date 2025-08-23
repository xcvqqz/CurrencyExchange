package io.github.xcvqqz.currencyexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class ConnectionFactory {

    private static final String DB_URL = "C:\\Users\\Ваня\\Desktop\\Java2\\3 проект\\CurrencyExchange\\DataBase\\exchange.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
            Connection connection = DriverManager.getConnection(DB_URL);
            return connection;
        }
}
