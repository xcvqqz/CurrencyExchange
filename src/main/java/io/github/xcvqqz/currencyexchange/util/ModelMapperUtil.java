package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;


public class ModelMapperUtil {

    public ModelMapperUtil(){}

    public CurrencyDto convertToDto(Currency currency){

        CurrencyDto currencyDto = new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());

        return currencyDto;
    }

    public ExchangeRateDto convertToDto(ExchangeRate exchangeRate){

        ExchangeRateDto exchangeRateDto = new ExchangeRateDto(
                exchangeRate.getId(),
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate());

        return exchangeRateDto;
    }
}

