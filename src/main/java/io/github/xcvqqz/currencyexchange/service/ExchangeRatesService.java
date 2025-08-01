package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesService {


    private final ExchangeRatesDao exchangeRatesDao;

    public ExchangeRatesService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    public List<ExchangeRates> getAllExchangeRates() throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.getAllExchangeRates();
    }

    public ExchangeRates getExchangeRates(String baseCode, String targetCode) throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.getExchangeRatePair(baseCode, targetCode);
    }

}
