package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {


    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Максим\\Desktop\\Java\\3 проект\\CurrencyExchange\\DataBase\\currency_exchange.db";
    private static final String JDBC_LOAD = "org.sqlite.JDBC";


    public List<ExchangeRates> getAllExchangeRates() throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_LOAD);
      List<ExchangeRates> result = new ArrayList<ExchangeRates>();

      try (Connection connection = DriverManager.getConnection(DB_URL);
           Statement statement = connection.createStatement();
           ResultSet rs = statement.executeQuery("select * from ExchangeRates")){

           while (rs.next()){
               result.add(new ExchangeRates(
                       rs.getInt("id"),
                       rs.getInt("baseCurrencyId"),
                       rs.getInt("targetCurrencyId"),
                       rs.getDouble("rate")
               ));
      }
    }
        return result;
}





}
