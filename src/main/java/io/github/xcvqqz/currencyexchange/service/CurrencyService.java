package io.github.xcvqqz.currencyexchange.service;


import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.util.ModelMapperUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {


    private ModelMapperUtil modelMapper;
    private CurrencyDao currencyDao;

    public CurrencyService (){
        modelMapper = new ModelMapperUtil();
        currencyDao = new CurrencyDao();
    }

    public List<CurrencyDto> findAll() {
        List<Currency> currencies = currencyDao.findAll();

        return currencies.stream()
                .map(modelMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto findByCode(String code) {
        Currency currency = currencyDao.findByCode(code).orElseThrow();
        return modelMapper.convertToDto(currency);
    }

    public CurrencyDto update(Currency currency) {
        return modelMapper.convertToDto(currencyDao.update(currency));
    }

    public CurrencyDto save(String code, String fullName, String sign) {
        return modelMapper.convertToDto(currencyDao.save(code, fullName, sign));
    }
}
