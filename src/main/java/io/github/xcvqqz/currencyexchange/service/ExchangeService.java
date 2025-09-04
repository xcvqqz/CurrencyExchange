package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.util.ModelMapperUtil;

import java.math.BigDecimal;

public class ExchangeService {

    private final ModelMapperUtil modelMapper;
    private final ExchangeRateService exchangeRateService;
    private static final String USD = "USD";


    public ExchangeService (String baseCode, String targetCode, BigDecimal amount){
        modelMapper = new ModelMapperUtil();
        exchangeRateService = new ExchangeRateService();
    }


    public ExchangeResponseDto convert(String fromCurrency, String toCurrency, BigDecimal amount){

        return findDirectRate(fromCurrency,toCurrency,amount);


    }



    //прямое получение курса
    private ExchangeResponseDto findDirectRate(String fromCurrency, String toCurrency, BigDecimal amount){

       ExchangeRateDto exchangeRateDto = exchangeRateService.getExchangeRatesPair(fromCurrency,toCurrency);
       BigDecimal rate = exchangeRateDto.rate();
       BigDecimal convertedAmount = amount.multiply(rate);

       return new ExchangeResponseDto(
               exchangeRateDto,
               rate,
               amount,
               convertedAmount);
    }



    //получение обратного курса
    private ExchangeResponseDto calculateInverseRate(String baseCode, String targetCode){

    }


    
    //получение кросс курса через USD
    private ExchangeResponseDto calculateCrossRateViaUsd(String baseCode, String targetCode, BigDecimal amount){

    }



}
