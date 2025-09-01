package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import org.modelmapper.ModelMapper;

import java.util.Objects;

public class ModelMapperUtil {

    private ModelMapper modelMapper;

    public ModelMapperUtil(){
        modelMapper = new ModelMapper();
    }

    public CurrencyDto convertToDto(Currency currency){
        return Objects.isNull(currency) ? null : modelMapper.map(currency, CurrencyDto.class);
    }

    public ExchangeRateDto convertToDto(ExchangeRate exchangeRate){
        return Objects.isNull(exchangeRate) ? null : modelMapper.map(exchangeRate, ExchangeRateDto.class);
    }
}
