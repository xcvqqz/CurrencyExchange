package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import org.modelmapper.ModelMapper;

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
}
