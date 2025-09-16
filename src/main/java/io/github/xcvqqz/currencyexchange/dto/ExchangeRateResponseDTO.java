package io.github.xcvqqz.currencyexchange.dto;

import io.github.xcvqqz.currencyexchange.entity.Currency;

import java.math.BigDecimal;

public record ExchangeRateResponseDTO(
        int id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate)
{}