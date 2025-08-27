package io.github.xcvqqz.currencyexchange.dao;


import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.exception.CurrencyNotFoundException;
import io.github.xcvqqz.currencyexchange.exception.DataBaseException;
import io.github.xcvqqz.currencyexchange.exception.EntityAlreadyExistException;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final String DB_ERROR_GET_ALL_CURRENCIES = "DatabaseError: Failed to fetch all currencies";
    private static final String DB_ERROR_FIND_BY_CODE = "Database operation failed: unable to find currency by code";
    private static final String DB_ERROR_UPDATE_CURRENCY = "Database error: Currency update operation failed";
    private static final String DB_ERROR_CREATE_CURRENCY_ONE = "Insert currency failed: no generated keys returned";
    private static final String DB_ERROR_CREATE_CURRENCY_TWO = "Database error: Failed to create new currency";
    private static final String DB_ERROR_ENTITY_ALREADY_EXIST = "A currency with these details already exists";
    private static final String DB_ERROR_CURRENCY_NOT_FOUND = "Currency with this code was not found";


    public List<CurrencyDto> getAllCurrencies() {

        String sqlQuery = "SELECT * FROM currencies";
        List<CurrencyDto> result = new ArrayList<>();


        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                result.add(new CurrencyDto(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign")
                ));
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_GET_ALL_CURRENCIES);
        }
        return result;
    }

    public Optional<CurrencyDto> findByCode(String code) {

        String sqlQuery = "SELECT * FROM currencies WHERE code = ?";
        Optional<CurrencyDto> result = Optional.empty();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    result = Optional.of(new CurrencyDto(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign")));
                } else {
                    throw new CurrencyNotFoundException(DB_ERROR_CURRENCY_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_FIND_BY_CODE);
        }
        return result;
    }


    public CurrencyDto updateCurrency(Currency currency){

        String checkExistSql = "SELECT id FROM currencies WHERE code = ? AND id != ?";
        String sqlQuery = "UPDATE currencies SET code = ?, fullName = ?, sign = ? WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkExistSql);
             PreparedStatement updateStmt = connection.prepareStatement(sqlQuery)) {

            checkStmt.setString(1, currency.getCode());
            checkStmt.setInt(2, currency.getId());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new EntityAlreadyExistException(DB_ERROR_ENTITY_ALREADY_EXIST);
                }
            }
            updateStmt.setString(1, currency.getCode());
            updateStmt.setString(2, currency.getFullName());
            updateStmt.setString(3, currency.getSign());
            updateStmt.setInt(4, currency.getId());

            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows == 0) {
                throw new CurrencyNotFoundException(DB_ERROR_CURRENCY_NOT_FOUND);
            }

            return new CurrencyDto(currency.getId(),
                    currency.getCode(),
                    currency.getFullName(),
                    currency.getSign());

        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_UPDATE_CURRENCY);
        }
    }

    public CurrencyDto createCurrency(String code, String fullName, String sign) {

        String sqlQuery = "INSERT INTO currencies (code, fullName, sign) VALUES (?, ?, ?)";
        String checkExistSql = "SELECT 1 FROM currencies WHERE code = ? AND fullName = ? AND sign = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkExistSql);
             PreparedStatement insertStmt = connection.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS)) {

            checkStmt.setString(1, code);
            checkStmt.setString(2, fullName);
            checkStmt.setString(3, sign);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new EntityAlreadyExistException("Failed to create currency: " + DB_ERROR_ENTITY_ALREADY_EXIST);
                }
            }

            insertStmt.setString(1, code);
            insertStmt.setString(2, fullName);
            insertStmt.setString(3, sign);
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new CurrencyDto(generatedId, code, fullName, sign);
                } else {
                    throw new DataBaseException(DB_ERROR_CREATE_CURRENCY_ONE);
                }
            }
    } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_CREATE_CURRENCY_TWO + e.getMessage());
        }
    }
}



