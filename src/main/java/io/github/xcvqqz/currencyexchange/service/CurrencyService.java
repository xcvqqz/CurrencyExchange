package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyDto> getAllCurrencies() throws ClassNotFoundException, SQLException {
        return currencyDao.getAllCurrencies();
    }

    public Optional<CurrencyDto> findByCode(String code) throws SQLException, ClassNotFoundException {
        return currencyDao.findByCode(code);
    }

    public CurrencyDto updateCurrency(Currency currency) throws SQLException, ClassNotFoundException {
        return currencyDao.updateCurrency(currency);
    }

    public CurrencyDto createCurrency(String code, String fullName, String sign) throws SQLException, ClassNotFoundException {
        return currencyDao.createCurrency(code, fullName, sign);
    }

}
