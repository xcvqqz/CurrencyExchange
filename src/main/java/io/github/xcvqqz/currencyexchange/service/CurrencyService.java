package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDAO;
import io.github.xcvqqz.currencyexchange.util.mapper.CurrencyMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {

    private CurrencyDAO currencyDao;
    private CurrencyMapper currencyMapper;

    public CurrencyService (){
        currencyMapper = new CurrencyMapper();
        currencyDao = new CurrencyDAO();
    }

    public List<CurrencyResponseDTO> findAll() {
        List<Currency> currencies = currencyDao.findAll();

        return currencies.stream()
                .map(currencyMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public Currency findByCode(String code) {
        return currencyDao.findByCode(code).orElseThrow();
    }

    public Currency update(Currency currency) {
        return currencyDao.update(currency);
    }

    public Currency save(String name, String code, String sign) {
        return currencyDao.save(name, code, sign);
    }
}