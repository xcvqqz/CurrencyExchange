package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;
import io.github.xcvqqz.currencyexchange.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    public List<ExchangeRatesDto> getAllExchangeRates() throws ClassNotFoundException, SQLException {

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


    public ExchangeRatesDto getExchangeRatePair(String baseCode, String targetCode) throws ClassNotFoundException, SQLException {

        Currency baseCurrency;
        Currency targetCurrency;
        ExchangeRatesDto result = null;

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

                    result = new ExchangeRatesDto(
                            rs.getInt("id"),
                            baseCurrency,
                            targetCurrency,
                            rs.getDouble("Rate"));
                }
            }
        }
        return result;
    }



public ExchangeRatesDto createExchangeRates(String baseCode, String targetCode, double rate) throws ClassNotFoundException, SQLException {

        Currency baseCurrency = null;
        Currency targetCurrency = null;

        String queryBaseCode = "SELECT * from currencies WHERE code = ?";
        String queryTargetCode = "SELECT * from currencies WHERE code = ?";
        String checkPairSql = "SELECT 1 FROM ExchangeRates WHERE baseCurrencyId = ? AND targetCurrencyId = ?";
        String sqlExecuteUpdate = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";


        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt1 = connection.prepareStatement(queryBaseCode);
            PreparedStatement stmt2 = connection.prepareStatement(queryTargetCode);
            PreparedStatement stmt3 = connection.prepareStatement(checkPairSql);
            PreparedStatement stmt4 = connection.prepareStatement(sqlExecuteUpdate);) {

            stmt1.setString(1, baseCode);
            stmt2.setString(1, targetCode);

            try (ResultSet rs = stmt1.executeQuery()) {
                while (rs.next()) {
                    baseCurrency = new Currency(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign"));
                }
            }

            try (ResultSet rs = stmt2.executeQuery()) {
                while (rs.next()) {
                    targetCurrency = new Currency(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign"));
                }
            }

            if (baseCurrency == null || targetCurrency == null) {
                throw new SQLException("One or both currencies not found");
            }

            stmt3.setInt(1, baseCurrency.getId());
            stmt3.setInt(2, targetCurrency.getId());
            try (ResultSet rs = stmt3.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("Exchange rate pair already exists");
                }
            }

            stmt4.setInt(1, baseCurrency.getId());
            stmt4.setInt(2, targetCurrency.getId());
            stmt4.setDouble(3, rate);
            stmt4.executeUpdate();
        }
            return getExchangeRatePair(baseCode, targetCode);
        }


    public ExchangeRatesDto updateExchangeRates(String baseCode, String targetCode, double rate) throws ClassNotFoundException, SQLException {

        Currency baseCurrency = null;
        Currency targetCurrency = null;

        String queryBaseСurrency = "SELECT * from currencies WHERE code = ?";
        String queryTargetCurrency = "SELECT * from currencies WHERE code = ?";
        String sqlUpdate = "UPDATE exchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";


        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmt1 = connection.prepareStatement(queryBaseСurrency);
            PreparedStatement stmt2 = connection.prepareStatement(queryTargetCurrency);
            PreparedStatement stmt3 = connection.prepareStatement(sqlUpdate)){

            stmt1.setString(1, baseCode);
            stmt2.setString(1, targetCode);


            try (ResultSet rs = stmt1.executeQuery()) {
                while (rs.next()) {
                    baseCurrency = new Currency(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign"));
                }
            }

            try (ResultSet rs = stmt2.executeQuery()) {
                while (rs.next()) {
                    targetCurrency = new Currency(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign"));
                }
            }

            if (baseCurrency == null || targetCurrency == null) {
                throw new SQLException("One or both currencies not found");
            }

            stmt3.setDouble(1, rate);
            stmt3.setInt(2, baseCurrency.getId());
            stmt3.setInt(3, targetCurrency.getId());
            stmt3.executeUpdate();

        }
        return getExchangeRatePair(baseCode, targetCode);
    }
}





