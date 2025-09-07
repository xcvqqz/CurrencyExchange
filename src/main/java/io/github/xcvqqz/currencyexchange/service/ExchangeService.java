package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDto;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.exception.CurrencyNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ExchangeService {
    private final ExchangeRateService exchangeRateService;
    private static final String USD = "USD";
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String CURRENCY_NOT_FOUND = "Currency not found";

    public ExchangeService() {
        this.exchangeRateService = new ExchangeRateService();
    }

    public ExchangeResponseDto convert(String from, String to, BigDecimal amount) {
        ConversionType conversionType = determineConversionType(from, to);

        return switch (conversionType) {
            case DIRECT -> convertDirect(from, to, amount);
            case INVERSE -> convertInverse(from, to, amount);
            case CROSS -> convertCross(from, to, amount);
            case NOT_AVAILABLE -> throw new CurrencyNotFoundException(CURRENCY_NOT_FOUND);
        };
    }


    private ConversionType determineConversionType(String from, String to) {

        if (isDirectRate(from, to)) {
            return ConversionType.DIRECT;
        }

        if (isInverseRate(from, to)) {
            return ConversionType.INVERSE;
        }

        if (isCrossRate(from, to)) {
            return ConversionType.CROSS;
        }

        return ConversionType.NOT_AVAILABLE;
    }

    private ExchangeResponseDto convertDirect(String from, String to, BigDecimal amount) {
        BigDecimal rate = getRate(from, to);
        BigDecimal convertedAmount = calculateDirect(amount, rate);
        ExchangeRateResponseDto rateDto = getExchangeRatePair(from, to);

        return createResponseDto(rateDto.baseCurrency(), rateDto.targetCurrency(),
                rate, amount, convertedAmount);
    }

    private ExchangeResponseDto convertInverse(String from, String to, BigDecimal amount) {
        BigDecimal inverseRate = getRate(to, from);
        BigDecimal rate = BigDecimal.ONE.divide(inverseRate, AMOUNT_SCALE, ROUNDING_MODE);
        BigDecimal convertedAmount = calculateDirect(amount, rate);
        ExchangeRateResponseDto rateDto = getExchangeRatePair(to, from);

        return createResponseDto(rateDto.targetCurrency(), rateDto.baseCurrency(),
                rate, amount, convertedAmount);
    }

    private ExchangeResponseDto convertCross(String from, String to, BigDecimal amount) {
        BigDecimal fromToUsdRate = getRate(USD, from);
        BigDecimal usdToToRate = getRate(USD, to);
        BigDecimal rate = usdToToRate.divide(fromToUsdRate, AMOUNT_SCALE, ROUNDING_MODE);

        BigDecimal convertedAmount = calculateDirect(amount, rate);

        Currency baseCurrency = getExchangeRatePair(USD, from).targetCurrency();
        Currency targetCurrency = getExchangeRatePair(USD, to).targetCurrency();

        return createResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }

    private BigDecimal calculateDirect(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(AMOUNT_SCALE, ROUNDING_MODE);
    }



    private boolean isDirectRate(String from, String to) {
        BigDecimal exchangeRate;
        try {
            exchangeRate = getRate(from, to);
        } catch(Exception e) {
            return false;
        }
        return exchangeRate.compareTo(BigDecimal.ONE) >= 1;
    }


    private boolean isInverseRate(String from, String to) {
        BigDecimal exchangeRate;
        try {
            exchangeRate = getRate(to, from);
        } catch(Exception e) {
            return false;
        }
        return exchangeRate.compareTo(BigDecimal.ONE) >= 1;
    }


    private boolean isCrossRate(String from, String to) {

        BigDecimal exchangeRateFromToUsd;
        BigDecimal exchangeRateUsdToTo;

        try {
       exchangeRateFromToUsd  = getRate(USD, from);
       exchangeRateUsdToTo  = getRate(USD, to);}
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private BigDecimal getRate(String from, String to) {
        return exchangeRateService.getExchangeRatesPair(from, to).rate();
    }

    private ExchangeRateResponseDto getExchangeRatePair(String from, String to) {
        return exchangeRateService.getExchangeRatesPair(from, to);
    }

    private ExchangeResponseDto createResponseDto(Currency base, Currency target,
                                                  BigDecimal rate, BigDecimal amount,
                                                  BigDecimal convertedAmount) {
        return new ExchangeResponseDto(base, target, rate, amount, convertedAmount);
    }

    private enum ConversionType {
        DIRECT, INVERSE, CROSS, NOT_AVAILABLE
    }

}
