package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dao.ExchangeRateDao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.util.ModelMapperUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRatesService {

    private ExchangeRateDao exchangeRatesDao;
    private ModelMapperUtil modelMapper;

    public ExchangeRatesService(){
        exchangeRatesDao = new ExchangeRateDao();
        modelMapper = new ModelMapperUtil();
    }

    public List<ExchangeRateDto> findAll()  {
        List<ExchangeRate> exchangeRates = exchangeRatesDao.findAll();

        return exchangeRates.stream()
                .map(modelMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto getExchangeRatesPair(String baseCode, String targetCode)  {
        ExchangeRate exchangeRate = exchangeRatesDao.getExchangeRatePair(baseCode, targetCode).orElseThrow();
        return modelMapper.convertToDto(exchangeRate);
    }

    public ExchangeRateDto save(String baseCode, String targetCode, double rate) {
        return modelMapper.convertToDto(exchangeRatesDao.save(baseCode, targetCode, rate));
    }

    public ExchangeRateDto update(String baseCode, String targetCode, double rate)  {
        return modelMapper.convertToDto(exchangeRatesDao.update(baseCode, targetCode, rate));
    }
}
