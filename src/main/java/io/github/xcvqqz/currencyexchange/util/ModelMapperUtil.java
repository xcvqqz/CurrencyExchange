package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;


public class ModelMapperUtil {

    public ModelMapperUtil(){}

    public CurrencyResponseDto convertToDto(Currency currency){

        CurrencyResponseDto currencyDto = new CurrencyResponseDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());

        return currencyDto;
    }

    public ExchangeRateResponseDto convertToDto(ExchangeRate exchangeRate){

        ExchangeRateResponseDto exchangeRateDto = new ExchangeRateResponseDto(
                exchangeRate.getId(),
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate());

        return exchangeRateDto;
    }
}

