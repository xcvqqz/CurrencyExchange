package io.github.xcvqqz.currencyexchange.dao;

import io.github.xcvqqz.currencyexchange.Currencies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao  {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Ваня\\Desktop\\Java\\Проекты\\3 проект\\CurrencyExchange\\DataBase\\exchange.db";


        public List<Currencies> getCurrencies() throws ClassNotFoundException {

            List<Currencies> result = new ArrayList<>();
            Class.forName("org.sqlite.JDBC");

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM currencies")){

                while(rs.next()){
                    result.add(new Currencies(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("fullName"),
                            rs.getString("sign")
                    ));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            return result;
        }
    }

