package io.github.xcvqqz.currencyexchange.util.mapper;

import io.github.xcvqqz.currencyexchange.dto.CurrencyRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.Currency;

public class CurrencyMapper  {

    public CurrencyMapper () {}

    public CurrencyResponseDTO convertToDto(Currency currency) {
        return new CurrencyResponseDTO(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }

    public Currency convertToCurrency(CurrencyRequestDTO currencyRequestDTO) {
        return new Currency(currencyRequestDTO.name(), currencyRequestDTO.code(), currencyRequestDTO.sign());
    }
}