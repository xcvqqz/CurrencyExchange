package io.github.xcvqqz.currencyexchange.exception;

public class CurrencyAlreadyExistException extends CurrencyExchangeException {
    public CurrencyAlreadyExistException(String message) {
        super(message);
    }
}
