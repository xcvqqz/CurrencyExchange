package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.exception.CurrencyNotFoundException;
import io.github.xcvqqz.currencyexchange.exception.DataBaseException;
import io.github.xcvqqz.currencyexchange.exception.EntityAlreadyExistException;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;
import io.github.xcvqqz.currencyexchange.util.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {

    private static final String DB_ERROR_GET_ALL_CURRENCIES = "DatabaseError: Failed to fetch all currencies";
    private static final String DB_ERROR_FIND_BY_CODE = "Database operation failed: unable to find currency by code";
    private static final String DB_ERROR_UPDATE_CURRENCY = "Database error: Currency update operation failed";
    private static final String DB_ERROR_CREATE_CURRENCY = "Database error: Failed to create new currency";
    private static final String DB_ERROR_ENTITY_ALREADY_EXIST = "A currency with these details already exists";
    private static final String DB_ERROR_CURRENCY_NOT_FOUND = "Currency with this code was not found";
    private static final String SQL_FIND_ALL_QUERY = "SELECT * FROM currencies";
    private static final String SQL_FIND_BY_CODE_QUERY = "SELECT * FROM currencies WHERE code = ?";
    private static final String SQL_UPDATE_QUERY = "UPDATE currencies SET code = ?, name = ?, sign = ? WHERE id = ?";
    private static final String SQL_SAVE_QUERY = "INSERT INTO currencies (name, code, sign) VALUES (?, ?, ?)";


    public List<Currency> findAll() {

        String sqlQuery = SQL_FIND_ALL_QUERY;
        List<Currency> result = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                result.add(new Currency(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getString("sign")
                ));
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_GET_ALL_CURRENCIES);
        }
        return result;
    }

    public Optional<Currency> findByCode(String code) {

        String sqlQuery = SQL_FIND_BY_CODE_QUERY;
        Optional<Currency> result;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    result = Optional.of(new Currency(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("code"),
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


    public Currency update(Currency currency){

        String sqlQuery = SQL_UPDATE_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(sqlQuery)) {

            updateStmt.setString(1, currency.getCode());
            updateStmt.setString(2, currency.getName());
            updateStmt.setString(3, currency.getSign());
            updateStmt.setInt(4, currency.getId());

            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows == 0) {
                throw new CurrencyNotFoundException(DB_ERROR_CURRENCY_NOT_FOUND);
            }

            return new Currency(currency.getId(),
                    currency.getName(),
                    currency.getCode(),
                    currency.getSign());

        } catch (SQLException e) {
            if (Validator.entityAlreadyValidation(e)) {
                throw new EntityAlreadyExistException(DB_ERROR_ENTITY_ALREADY_EXIST);
            }
            throw new DataBaseException(DB_ERROR_UPDATE_CURRENCY);
        }
    }


    public Currency save(String name, String code, String sign) {

        String sqlQuery = SQL_SAVE_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, name);
            insertStmt.setString(2, code);
            insertStmt.setString(3, sign);

            try {
                insertStmt.executeUpdate();
                return getGeneratedCurrency(insertStmt, name, code, sign);
            } catch (SQLException e) {
                if (Validator.entityAlreadyValidation(e)) {
                    throw new EntityAlreadyExistException(DB_ERROR_ENTITY_ALREADY_EXIST);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_CREATE_CURRENCY);
        }
    }


    private Currency getGeneratedCurrency(PreparedStatement stmt, String name, String code,  String sign) throws SQLException {
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            return new Currency(generatedId, name, code, sign);
        } else {
            throw new DataBaseException(DB_ERROR_CREATE_CURRENCY);
        }
    }
}