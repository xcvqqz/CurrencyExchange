package io.github.xcvqqz.currencyexchange.dao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.exception.EntityAlreadyExistException;
import io.github.xcvqqz.currencyexchange.exception.DataBaseException;
import io.github.xcvqqz.currencyexchange.exception.ExchangeRateNotFoundException;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao {


    private static final String DB_ERROR_GET_ALL_EXCHANGE_RATES = "DatabaseError: Failed to fetch all exchange rates";
    private static final String EXCHANGE_RATE_PAIR_NOT_FOUND = "Exchange rate pair not found.";
    private static final String DB_ERROR_RETRIEVING_EXCHANGE_RATE = "Database error: Failed to retrieve the exchange rate.";
    private static final String EXCHANGE_RATE_PAIR_ALREADY_EXISTS = "Error: This exchange rate pair already exists.";
    private static final String DB_ERROR_CREATING_EXCHANGE_RATE = "Database error: Failed to create a new exchange rate.";
    private static final String DB_ERROR_UPDATE_EXCHANGE_RATE = "Database error: Failed to update a exchange rate.";
    private static final String EXCHANGE_RATE_PAIR_NOT_FOUND_FOR_UPDATE = "Exchange rate pair not found for update rate.";


    public List<ExchangeRatesDto> getAllExchangeRates() {

        List<ExchangeRatesDto> result = new ArrayList<>();
        String sql = "SELECT " +
                "er.id, " +
                "base.id AS IdBaseCurrency, " + "base.code AS CodeBaseCurrency, " + "base.fullName AS FullNameBaseCurrency, " + "base.sign AS SignBaseCurrency, " +
                "target.id AS IdTargetCurrency, " + "target.code AS CodeTargetCurrency, " + "target.fullName AS FullNameTargetCurrency, " + "target.sign AS SignTargetCurrency, " +
                "er.Rate " +
                "FROM ExchangeRates er " +
                "JOIN currencies base ON er.BaseCurrencyId = base.id " +
                "JOIN currencies target ON er.TargetCurrencyId = target.id;";

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Currency BaseCurrency = new Currency(
                        rs.getInt("IdBaseCurrency"),
                        rs.getString("CodeBaseCurrency"),
                        rs.getString("FullNameBaseCurrency"),
                        rs.getString("SignBaseCurrency"));

                Currency TargetCurrency = new Currency(
                        rs.getInt("IdTargetCurrency"),
                        rs.getString("CodeTargetCurrency"),
                        rs.getString("FullNameTargetCurrency"),
                        rs.getString("SignTargetCurrency"));

                result.add(new ExchangeRatesDto(
                        rs.getInt("id"),
                        BaseCurrency,
                        TargetCurrency,
                        rs.getDouble("Rate")));
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_GET_ALL_EXCHANGE_RATES);
        }

        if (result.isEmpty()) {
            throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND);
        }

        return result;
    }


    public Optional<ExchangeRatesDto> getExchangeRatePair(String baseCode, String targetCode) {

        Currency baseCurrency;
        Currency targetCurrency;
        Optional<ExchangeRatesDto> result = Optional.empty();

        String sql = "SELECT " +
                "er.id, " +
                "base.id AS IdBaseCurrency, " + "base.code AS CodeBaseCurrency, " + "base.fullName AS FullNameBaseCurrency, " + "base.sign AS SignBaseCurrency, " +
                "target.id AS IdTargetCurrency, " + "target.code AS CodeTargetCurrency, " + "target.fullName AS FullNameTargetCurrency, " + "target.sign AS SignTargetCurrency, " +
                "er.Rate " +
                "FROM ExchangeRates er " +
                "JOIN currencies base ON er.BaseCurrencyId = base.id " +
                "JOIN currencies target ON er.TargetCurrencyId = target.id " +
                "WHERE CodeBaseCurrency = ? AND CodeTargetCurrency = ?";


        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    baseCurrency = new Currency(
                            rs.getInt("IdBaseCurrency"),
                            rs.getString("CodeBaseCurrency"),
                            rs.getString("FullNameBaseCurrency"),
                            rs.getString("SignBaseCurrency"));

                    targetCurrency = new Currency(
                            rs.getInt("IdTargetCurrency"),
                            rs.getString("CodeTargetCurrency"),
                            rs.getString("FullNameTargetCurrency"),
                            rs.getString("SignTargetCurrency"));

                  result = Optional.of(new ExchangeRatesDto(
                            rs.getInt("id"),
                            baseCurrency,
                            targetCurrency,
                            rs.getDouble("Rate")));
                } else {
                    throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_RETRIEVING_EXCHANGE_RATE);
        }
        return result;
    }


    public ExchangeRatesDto createExchangeRates(String baseCode, String targetCode, double rate) {

        String sqlQueryByCode = "SELECT * from currencies WHERE code = ?";
        String checkExistSql = "SELECT 1 FROM ExchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";
        String sqlExecuteUpdate = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection()) {

            Optional<Currency> baseCurrency = findCurrency(connection, baseCode, sqlQueryByCode);
            Optional<Currency> targetCurrency = findCurrency(connection, targetCode, sqlQueryByCode);

            try (PreparedStatement checkStmt = connection.prepareStatement(checkExistSql);
                 PreparedStatement insertStmt = connection.prepareStatement(sqlExecuteUpdate,Statement.RETURN_GENERATED_KEYS);) {

                checkStmt.setInt(1, baseCurrency.get().getId());
                checkStmt.setInt(2, targetCurrency.get().getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new EntityAlreadyExistException(EXCHANGE_RATE_PAIR_ALREADY_EXISTS);
                    }
                }

                insertStmt.setInt(1, baseCurrency.get().getId());
                insertStmt.setInt(2, targetCurrency.get().getId());
                insertStmt.setDouble(3, rate);
                insertStmt.executeUpdate();


                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return new ExchangeRatesDto(generatedId, baseCurrency.orElse(null), targetCurrency.orElse(null), rate);
                    } else {
                        throw new DataBaseException(DB_ERROR_CREATING_EXCHANGE_RATE);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException( DB_ERROR_CREATING_EXCHANGE_RATE);
        }
    }


        public ExchangeRatesDto updateExchangeRates (String baseCode, String targetCode,double rate) {

            String sqlQueryByCode = "SELECT * from currencies WHERE code = ?";
            String sqlUpdate = "UPDATE exchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
            String checkExistSql = "SELECT 1 FROM ExchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";
            Optional<Currency> baseCurrency =  Optional.empty();
            Optional<Currency> targetCurrency =  Optional.empty();

            try (Connection connection = ConnectionFactory.getConnection()){

                baseCurrency = findCurrency(connection, baseCode, sqlQueryByCode);
                targetCurrency = findCurrency(connection, targetCode, sqlQueryByCode);

                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate);
                     PreparedStatement checkStmt = connection.prepareStatement(checkExistSql)){

                    checkStmt.setInt(1, baseCurrency.get().getId());
                    checkStmt.setInt(2, targetCurrency.get().getId());
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new ExchangeRateNotFoundException(EXCHANGE_RATE_PAIR_NOT_FOUND_FOR_UPDATE);
                        }
                    }

                updateStmt.setDouble(1, rate);
                updateStmt.setInt(2, baseCurrency.get().getId());
                updateStmt.setInt(3, targetCurrency.get().getId());
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
                throw new DataBaseException(DB_ERROR_UPDATE_EXCHANGE_RATE);
            }
            return new ExchangeRatesDto(0, baseCurrency.orElse(null),
                    targetCurrency.orElse(null), rate);
        }

    private static Optional<Currency> findCurrency (Connection connection, String code, String sqlQuery) throws SQLException {

        Optional<Currency> result = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = Optional.of(new Currency(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign")
                    ));
                }
            }
        }
        return result;
    }
}



