package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesService {


    private final ExchangeRatesDao exchangeRatesDao;

    public ExchangeRatesService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    public List<ExchangeRatesDto> getAllExchangeRates() throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.getAllExchangeRates();
    }

    public ExchangeRatesDto getExchangeRates(String baseCode, String targetCode) throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.getExchangeRatePair(baseCode, targetCode);
    }

    public ExchangeRatesDto createExchangeRates(String baseCode, String targetCode, double rate) throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.createExchangeRates(baseCode, targetCode, rate);
    }

    public ExchangeRatesDto updateExchangeRates(String baseCode, String targetCode, double rate) throws SQLException, ClassNotFoundException {
        return exchangeRatesDao.updateExchangeRates(baseCode, targetCode, rate);
    }

}
