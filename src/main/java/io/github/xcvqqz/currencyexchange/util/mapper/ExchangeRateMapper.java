package io.github.xcvqqz.currencyexchange.util.mapper;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;

public class ExchangeRateMapper {

    public ExchangeRateMapper() {}

    public ExchangeRateResponseDTO convertToDto(ExchangeRate exchangeRate) {
        return new ExchangeRateResponseDTO(exchangeRate.getId(), exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate());
    }

    public ExchangeRate convertToExchangeRate(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        return new ExchangeRate(
                new CurrencyService().findByCode(exchangeRateRequestDTO.baseCurrencyCode()),
                new CurrencyService().findByCode(exchangeRateRequestDTO.targetCurrencyCode()),
                exchangeRateRequestDTO.rate()
        );
    }
}