package io.github.xcvqqz.currencyexchange.dto;

import io.github.xcvqqz.currencyexchange.entity.Currency;

public record ExchangeRatesDto(
        int id,
        Currency baseCurrency,
        Currency targetCurrency,
        double rate)

{}
