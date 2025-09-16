package io.github.xcvqqz.currencyexchange.dto;

public record CurrencyRequestDTO (
    String name,
    String code,
    String sign)
{}