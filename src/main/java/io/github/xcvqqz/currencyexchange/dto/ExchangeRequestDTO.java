package io.github.xcvqqz.currencyexchange.dto;

import java.math.BigDecimal;

public record ExchangeRequestDTO(
        String from,
        String to,
        BigDecimal amount)
{}