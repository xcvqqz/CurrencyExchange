package io.github.xcvqqz.currencyexchange.dto;

import java.math.BigDecimal;

public record ExchangeRateRequestDTO(int id, String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {

    public ExchangeRateRequestDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this(0, baseCurrencyCode, targetCurrencyCode, rate);
    }
}