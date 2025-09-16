package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dao.CurrencyDAO;
import io.github.xcvqqz.currencyexchange.dao.ExchangeRateDAO;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.util.mapper.ExchangeRateMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private ExchangeRateDAO exchangeRatesDao;
    private ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateService() {
        exchangeRatesDao = new ExchangeRateDAO();
        exchangeRateMapper = new ExchangeRateMapper();
    }

    public List<ExchangeRateResponseDTO> findAll() {
        List<ExchangeRate> exchangeRates = exchangeRatesDao.findAll();

        return exchangeRates.stream()
                .map(exchangeRateMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public ExchangeRate getExchangeRatesPair(String baseCode, String targetCode) {
        return exchangeRatesDao.getExchangeRatePair(baseCode, targetCode).orElseThrow();
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        return exchangeRatesDao.save(exchangeRate);
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        return exchangeRatesDao.update(exchangeRate);
    }
}