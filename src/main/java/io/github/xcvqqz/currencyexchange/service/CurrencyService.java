package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;

import java.util.List;

public class CurrencyService {

    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<Currency> getCurrencies() throws ClassNotFoundException {
        return currencyDao.getCurrencies();
    }

}
