package io.github.xcvqqz.currencyexchange.dto;

import io.github.xcvqqz.currencyexchange.entity.Currency;

public record ExchangeRateDto(
        int id,
        Currency baseCurrency,
        Currency targetCurrency,
        double rate)

{}
