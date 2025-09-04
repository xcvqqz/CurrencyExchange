package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;

import java.math.BigDecimal;

public class ExchangeService {

    private final ExchangeRateService exchangeRateService;
    private static final String USD = "USD";

    public ExchangeService (){
        exchangeRateService = new ExchangeRateService();
    }


    public ExchangeResponseDto convert(String from, String to, BigDecimal amount) {
        return findDirectRate(from, to, amount);
    }



    //прямое получение курса
    private ExchangeResponseDto findDirectRate(String from, String to, BigDecimal amount){

       ExchangeRateDto exchangeRateDto = exchangeRateService.getExchangeRatesPair(from,to);
       Currency baseCurrency = exchangeRateDto.baseCurrency();
       Currency targetCurrency = exchangeRateDto.targetCurrency();
       BigDecimal rate = exchangeRateDto.rate();
       BigDecimal convertedAmount = amount.multiply(rate);

       return new ExchangeResponseDto(
               baseCurrency,
               targetCurrency,
               rate,
               amount,
               convertedAmount);
    }



    //получение обратного курса
//    private ExchangeResponseDto calculateInverseRate(String baseCode, String targetCode){
//
//    }


    
    //получение кросс курса через USD
//    private ExchangeResponseDto calculateCrossRateViaUsd(String baseCode, String targetCode, BigDecimal amount){
//
//    }



}
