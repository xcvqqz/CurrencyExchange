package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.entity.Currency;

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


    public Currency findByCode(String code) throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_LOAD);
        Currency currency;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?");){
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {

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
        }
        return currency;
    }


    public Currency updateCurrency(Currency currency) throws ClassNotFoundException, SQLException {

        String sql = "UPDATE currencies SET code = ?, fullName = ?, sign = ? WHERE id = ?";
        Class.forName(JDBC_LOAD);


        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            stmt.setInt(4, currency.getId());
            stmt.executeUpdate();
        }
        return findByCode(currency.getCode());
    }

    public Currency createCurrency(String code, String fullName, String sign) throws ClassNotFoundException, SQLException {

        String sql = "INSERT INTO currencies (code, fullName, sign) VALUES (?, ?, ?)";
        Class.forName(JDBC_LOAD);
        try (Connection connection = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, fullName);
            stmt.setString(3, sign);
            stmt.executeUpdate();
        }
        return findByCode(code);
    }
}

