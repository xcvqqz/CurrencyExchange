package io.github.xcvqqz.currencyexchange.dto;

import java.math.BigDecimal;
import io.github.xcvqqz.currencyexchange.entity.Currency;

public record ExchangeResponseDto(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount)

{}
