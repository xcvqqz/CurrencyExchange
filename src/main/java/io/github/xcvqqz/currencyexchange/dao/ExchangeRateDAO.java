package io.github.xcvqqz.currencyexchange.dao;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.exception.EntityAlreadyExistException;
import io.github.xcvqqz.currencyexchange.exception.DataBaseException;
import io.github.xcvqqz.currencyexchange.exception.ExchangeRateNotFoundException;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;
import io.github.xcvqqz.currencyexchange.util.Validator;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO {

    private static final String DB_ERROR_GET_ALL_EXCHANGE_RATES = "DatabaseError: Failed to fetch all exchange rates";
    private static final String EXCHANGE_RATE_PAIR_NOT_FOUND = "Exchange rate pair not found.";
    private static final String DB_ERROR_RETRIEVING_EXCHANGE_RATE = "Database error: Failed to retrieve the exchange rate.";
    private static final String EXCHANGE_RATE_PAIR_ALREADY_EXISTS = "Error: This exchange rate pair already exists.";
    private static final String DB_ERROR_CREATING_EXCHANGE_RATE = "Database error: Failed to create a new exchange rate.";
    private static final String DB_ERROR_UPDATE_EXCHANGE_RATE = "Database error: Failed to update a exchange rate.";

    private static final String SQL_FIND_ALL_QUERY = "SELECT " +
            "er.id, " +
            "base.id AS IdBaseCurrency, " + "base.code AS CodeBaseCurrency, " + "base.name AS NameBaseCurrency, " + "base.sign AS SignBaseCurrency, " +
            "target.id AS IdTargetCurrency, " + "target.code AS CodeTargetCurrency, " + "target.name AS NameTargetCurrency, " + "target.sign AS SignTargetCurrency, " +
            "er.rate " +
            "FROM exchange_rates er " +
            "JOIN currencies base ON er.baseCurrencyId = base.id " +
            "JOIN currencies target ON er.targetCurrencyId = target.id;";

    private static final String SQL_GET_EXCHANGERATE_PAIR_QUERY = "SELECT " +
            "er.id, " +
            "base.id AS IdBaseCurrency, " + "base.code AS CodeBaseCurrency, " + "base.name AS NameBaseCurrency, " + "base.sign AS SignBaseCurrency, " +
            "target.id AS IdTargetCurrency, " + "target.code AS CodeTargetCurrency, " + "target.name AS NameTargetCurrency, " + "target.sign AS SignTargetCurrency, " +
            "er.rate " +
            "FROM exchange_rates er " +
            "JOIN currencies base ON er.baseCurrencyId = base.id " +
            "JOIN currencies target ON er.targetCurrencyId = target.id " +
            "WHERE CodeBaseCurrency = ? AND CodeTargetCurrency = ?";

    private static final String SQL_SAVE_QUERY = "INSERT INTO exchange_rates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE exchange_rates SET rate = ? WHERE baseCurrencyId = ? AND targetCurrencyId = ?";



    public List<ExchangeRate> findAll() {

        List<ExchangeRate> result = new ArrayList<>();
        String sql = SQL_FIND_ALL_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Currency BaseCurrency = new Currency(
                        rs.getInt("IdBaseCurrency"),
                        rs.getString("NameBaseCurrency"),
                        rs.getString("CodeBaseCurrency"),
                        rs.getString("SignBaseCurrency"));

                Currency TargetCurrency = new Currency(
                        rs.getInt("IdTargetCurrency"),
                        rs.getString("NameTargetCurrency"),
                        rs.getString("CodeTargetCurrency"),
                        rs.getString("SignTargetCurrency"));

                result.add(new ExchangeRate(
                        rs.getInt("id"),
                        BaseCurrency,
                        TargetCurrency,
                        rs.getBigDecimal("rate")));
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_GET_ALL_EXCHANGE_RATES);
        }

        if (result.isEmpty()) {
            throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND);
        }

        return result;
    }


    public Optional<ExchangeRate> getExchangeRatePair(String baseCode, String targetCode) {

        Currency baseCurrency;
        Currency targetCurrency;
        Optional<ExchangeRate> result;

        String sqlQuery = SQL_GET_EXCHANGERATE_PAIR_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    baseCurrency = new Currency(
                            rs.getInt("IdBaseCurrency"),
                            rs.getString("NameBaseCurrency"),
                            rs.getString("CodeBaseCurrency"),
                            rs.getString("SignBaseCurrency"));

                    targetCurrency = new Currency(
                            rs.getInt("IdTargetCurrency"),
                            rs.getString("NameTargetCurrency"),
                            rs.getString("CodeTargetCurrency"),
                            rs.getString("SignTargetCurrency"));

                    result = Optional.of(new ExchangeRate(
                            rs.getInt("id"),
                            baseCurrency,
                            targetCurrency,
                            rs.getBigDecimal("rate")));
                } else {
                    throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_RETRIEVING_EXCHANGE_RATE);
        }
        return result;
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {

        String sqlQuery = SQL_SAVE_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(sqlQuery,Statement.RETURN_GENERATED_KEYS)) {

            try {
                insertStmt.setInt(1, exchangeRate.getBaseCurrency().getId());
                insertStmt.setInt(2, exchangeRate.getTargetCurrency().getId());
                insertStmt.setBigDecimal(3, exchangeRate.getRate());
                insertStmt.executeUpdate();
                return getGeneratedExchangeRate(insertStmt, exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(),exchangeRate.getRate());
            } catch (SQLException e) {
                if (Validator.entityAlreadyValidation(e)) {
                    throw new EntityAlreadyExistException(EXCHANGE_RATE_PAIR_ALREADY_EXISTS);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_CREATING_EXCHANGE_RATE);
        }
    }

    public ExchangeRate update (ExchangeRate exchangeRate) {

        String sqlUpdate = SQL_UPDATE_QUERY;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {

            updateStmt.setBigDecimal(1, exchangeRate.getRate());
            updateStmt.setInt(2, exchangeRate.getBaseCurrency().getId());
            updateStmt.setInt(3, exchangeRate.getTargetCurrency().getId());

            int affectedRows = updateStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND);
            }

            return exchangeRate;

        } catch (SQLException e) {
            if (Validator.entityAlreadyValidation(e)) {
                throw new EntityAlreadyExistException(EXCHANGE_RATE_PAIR_ALREADY_EXISTS);
            }
            throw new DataBaseException(DB_ERROR_UPDATE_EXCHANGE_RATE);
        }
    }

    private ExchangeRate getGeneratedExchangeRate(PreparedStatement stmt, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) throws SQLException {
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            return new ExchangeRate(generatedId, baseCurrency, targetCurrency, rate);
        } else {
            throw new DataBaseException(DB_ERROR_CREATING_EXCHANGE_RATE);
        }
    }
}