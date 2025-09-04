package io.github.xcvqqz.currencyexchange.dto;

public record CurrencyResponseDto(
        int id,
        String code,
        String fullName,
        String sign)
{}

