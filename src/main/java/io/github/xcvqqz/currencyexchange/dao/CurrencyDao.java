package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Максим\\Desktop\\Java\\3 проект\\CurrencyExchange\\DataBase\\currency_exchange.db";
    private static final String JDBC_LOAD = "org.sqlite.JDBC";


    public List<Currency> getAllCurrencies() throws ClassNotFoundException {

        List<Currency> result = new ArrayList<>();
        Class.forName(JDBC_LOAD);

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM currencies")) {

            while (rs.next()) {
                result.add(new Currency(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Currency getCurrencyByCode(String code) throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_LOAD);
        Currency currency;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM currencies WHERE code = ?")) {

            if (rs.next()) {
                currency = new Currency(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign"));
            } else {
                throw new IllegalArgumentException("Currency not found: " + code);
            }
        }
        return currency;
    }


    public boolean updateCurrency(Currency currency) throws ClassNotFoundException, SQLException {

        String sql = "UPDATE currencies SET code = ?, fullName = ?, sign = ?, id = ?";
        Class.forName(JDBC_LOAD);

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            stmt.setInt(4, currency.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean createCurrency(Currency currency) throws ClassNotFoundException, SQLException {

        String  sql = "INSERT INTO currencies (code, fullName, sign) VALUES (?, ?, ?)";
        Class.forName(JDBC_LOAD);
        try (Connection connection = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            return stmt.executeUpdate() > 0;
        }
    }
}

