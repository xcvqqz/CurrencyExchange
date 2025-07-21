package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<Currency> getAllCurrencies() throws ClassNotFoundException {
        return currencyDao.getAllCurrencies();
    }

    public Currency findByCode(String code) throws SQLException, ClassNotFoundException {
        return currencyDao.findByCode(code);
    }

    public Currency updateCurrency(Currency currency) throws SQLException, ClassNotFoundException {
        return currencyDao.updateCurrency(currency);
    }

    public Currency createCurrency(String code, String fullName, String sign) throws SQLException, ClassNotFoundException {
        return currencyDao.createCurrency(code, fullName, sign);
    }

}
