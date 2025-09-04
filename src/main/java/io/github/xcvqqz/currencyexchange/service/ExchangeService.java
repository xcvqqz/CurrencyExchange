package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {

    private final ExchangeRateService exchangeRateService;
    private static final String USD = "USD";

    public ExchangeService (){
        exchangeRateService = new ExchangeRateService();
    }


    public ExchangeResponseDto convert(String from, String to, BigDecimal amount) {
        return calculateDirectRate(from, to, amount);
//        return calculateInverseRate(from, to, amount);
    }



    //прямое получение курса
    private ExchangeResponseDto calculateDirectRate(String from, String to, BigDecimal amount){

       ExchangeRateResponseDto exchangeRateDto = exchangeRateService.getExchangeRatesPair(from,to);
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
    private ExchangeResponseDto calculateInverseRate(String from, String to, BigDecimal amount){

        ExchangeRateResponseDto exchangeRateDto = exchangeRateService.getExchangeRatesPair(to,from);
        Currency baseCurrency = exchangeRateDto.baseCurrency();
        Currency targetCurrency = exchangeRateDto.targetCurrency();

        BigDecimal rate = exchangeRateDto.rate();
        BigDecimal reverseRate  = BigDecimal.ONE.divide(rate, 2, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = amount.multiply(reverseRate).setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResponseDto(
                baseCurrency,
                targetCurrency,
                rate,
                amount,
                convertedAmount);
    }


    
    //получение кросс курса через USD
//    private ExchangeResponseDto calculateCrossRateViaUsd(String baseCode, String targetCode, BigDecimal amount){
//
//    }


}
