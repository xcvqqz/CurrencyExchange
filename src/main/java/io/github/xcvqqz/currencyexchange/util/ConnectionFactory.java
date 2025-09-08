package io.github.xcvqqz.currencyexchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    //private static final String DB_URL = "jdbc:sqlite:C:\\Users\\Максим\\Desktop\\Java\\3 проект\\CurrencyExchange\\DataBase\\currency_exchange.db";


    private static final HikariDataSource dataSource;
    private static final HikariConfig config;

    static {

        config = new HikariConfig();

        String dataBasePath = ConnectionFactory.class.getClassLoader().getResource("").getPath();
        config.setJdbcUrl("jdbc:sqlite:" + dataBasePath);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000); // 30 секунд
        config.setMaxLifetime(1800000); // 30 минут
        config.setPoolName("CurrencyExchangePool");

        dataSource = new HikariDataSource(config);

    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getDataSource(){
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }


}
