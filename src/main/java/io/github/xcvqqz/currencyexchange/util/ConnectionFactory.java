package io.github.xcvqqz.currencyexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Максим\\Desktop\\Java\\3 проект\\CurrencyExchange\\DataBase\\currency_exchange.db";

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
