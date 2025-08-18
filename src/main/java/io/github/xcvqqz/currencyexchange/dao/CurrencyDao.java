package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    static {
        try {
            Class.forName("org.sqlite.JDBC"); // Загрузка драйвера при старте приложения
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    public List<CurrencyDto> getAllCurrencies() throws ClassNotFoundException, SQLException {

        List<CurrencyDto> result = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM currencies")) {

            while (rs.next()) {
                result.add(new CurrencyDto(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign")
                ));
            }
        }
        return result;
    }


    public Optional<CurrencyDto> findByCode(String code) throws IllegalArgumentException, SQLException {

        Optional<CurrencyDto> result = Optional.empty();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM currencies WHERE code = ?");) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    result = Optional.of(new CurrencyDto(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign")));
                }
            }
        }
        return result;
    }


    public CurrencyDto updateCurrency(Currency currency) throws SQLException {

        String sql = "UPDATE currencies SET code = ?, fullName = ?, sign = ? WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            stmt.setInt(4, currency.getId());
            stmt.executeUpdate();
        }

        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign()
        );
    }

    public CurrencyDto createCurrency(String code, String fullName, String sign) throws SQLException {

        String sql = "INSERT INTO currencies (code, fullName, sign) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, fullName);
            stmt.setString(3, sign);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new CurrencyDto(generatedId, code, fullName, sign);
                } else {
                    throw new SQLException("Creating currency failed: no ID obtained.");
                }
            }
        }
    }



}



