package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

public class ModelMapperUtil {

    private ModelMapper modelMapper;

    public ModelMapperUtil(){
        modelMapper = new ModelMapper();
    }

    public CurrencyDto convertToDto(Currency currency){
        return modelMapper.map(currency, CurrencyDto.class);
    }

    public ExchangeRateDto convertToDto(ExchangeRate exchangeRate){
        return modelMapper.map(exchangeRate, ExchangeRateDto.class);
    }

    public Currency convertToCurrency(CurrencyDto currencyDto){
        return modelMapper.map(currencyDto, Currency.class);
    }

    public ExchangeRate convertToExchangeRate(ExchangeRateDto exchangeRateDto){
        return modelMapper.map(exchangeRateDto, ExchangeRate.class);
    }

    public ExchangeResponseDto convertToDto(){

        return new ExchangeResponseDto(
                Currency baseCurrency,
                Currency targetCurrency,
                BigDecimal rate,
                BigDecimal amount,
                BigDecimal convertedAmount
        )
    }

}
