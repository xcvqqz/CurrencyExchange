package io.github.xcvqqz.currencyexchange.dto;

public record CurrencyResponseDTO(
        int id,
        String name,
        String code,
        String sign)
{}