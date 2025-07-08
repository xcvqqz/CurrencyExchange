package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.Currencies;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;

import java.util.List;

public class CurrencyService {

    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<Currencies> getCurrencies() throws ClassNotFoundException {
        return currencyDao.getCurrencies();
    }

}
