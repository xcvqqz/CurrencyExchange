package io.github.xcvqqz.currencyexchange.dao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao {


    public List<ExchangeRatesDto> getAllExchangeRates() throws SQLException {

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
        }
        return result;
    }


    public Optional<ExchangeRatesDto> getExchangeRatePair(String baseCode, String targetCode) throws SQLException {

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

                while (rs.next()) {
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
                }
            }
        }
        return result;
    }


    public ExchangeRatesDto createExchangeRates(String baseCode, String targetCode, double rate) throws SQLException {

        String queryByCode = "SELECT * from currencies WHERE code = ?";
        String checkPairSql = "SELECT 1 FROM ExchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";
        String sqlExecuteUpdate = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";


        try (Connection connection = ConnectionFactory.getConnection()) {

            Optional<Currency> baseCurrency = findCurrency(connection, baseCode, queryByCode);
            Optional<Currency> targetCurrency = findCurrency(connection, targetCode, queryByCode);

            try (PreparedStatement checkStmt = connection.prepareStatement(checkPairSql);
                 PreparedStatement insertStmt = connection.prepareStatement(sqlExecuteUpdate);) {

                if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                    throw new SQLException("One or both currencies not found");
                }

                checkStmt.setInt(1, baseCurrency.get().getId());
                checkStmt.setInt(2, targetCurrency.get().getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException("Exchange rate pair already exists");
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
                        throw new SQLException("Creating exchangeRates failed: no ID obtained.");
                    }
                }
            }
        }
    }


        public ExchangeRatesDto updateExchangeRates (String baseCode, String targetCode,double rate) throws SQLException {


            String queryByCode = "SELECT * from currencies WHERE code = ?";
            String sqlUpdate = "UPDATE exchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";


            try (Connection connection = ConnectionFactory.getConnection()){

                Optional<Currency> baseCurrency = findCurrency(connection, baseCode, queryByCode);
                Optional<Currency> targetCurrency = findCurrency(connection, targetCode, queryByCode);

                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {

                if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                    throw new SQLException("One or both currencies not found");
                }

                updateStmt.setDouble(1, rate);
                updateStmt.setInt(2, baseCurrency.get().getId());
                updateStmt.setInt(3, targetCurrency.get().getId());
                updateStmt.executeUpdate();

                try (ResultSet generatedKeys = updateStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return new ExchangeRatesDto(generatedId, baseCurrency.orElse(null), targetCurrency.orElse(null), rate);
                    } else {
                        throw new SQLException("Creating exchangeRates failed: no ID obtained.");
                    }
                }
            }
        }
}

    public static Optional<Currency> findCurrency (Connection connection, String code, String sql) throws SQLException {

        Optional<Currency> result = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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



