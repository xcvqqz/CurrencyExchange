package io.github.xcvqqz.currencyexchange.dto;

import io.github.xcvqqz.currencyexchange.entity.Currency;

import java.math.BigDecimal;

public record ExchangeResponseDto(
        ExchangeRateDto exchangeRateDto,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount)

{}
