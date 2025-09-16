package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.exception.ValidationException;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Validator {

    private Validator(){}

    private static final String ISO_4217 ="^[A-Z]{3}$";
    private static final String FULL_NAME = "^[\\p{L} .'-]{1,30}$";
    private static final String SIGN = "^[\\p{Sc}\\p{So}A-Za-z]{1,3}$";
    private static final String INVALID_CODE_MESSAGE = "Invalid code = %s entered.";
    private static final String INVALID_EXCHANGE_RATE_INPUT_MESSAGE = "Incorrect baseCode = %s, targetCode = %s, or rate = %f value entered.";
    private static final String INVALID_EXCHANGE_INPUT_MESSAGE = "Incorrect baseCode = %s or targetCode = %s value entered.";
    private static final String INVALID_CURRENCY_FIELDS_MESSAGE = "Incorrect code = %s, name = %s, or sign = %s value entered.";
    private static final String INVALID_PATH_INFO_MESSAGE = "Invalid URL format = %s";
    private static final int SQLITE_ERROR_CODE = 19;
    private static final String SQLITE_ERROR_MESSAGE = "UNIQUE constraint failed";


    public static void validate(String code) {
        code = code.toUpperCase().trim();
        if (code == null || !code.matches(ISO_4217)) {
            throw new ValidationException(String.format(INVALID_CODE_MESSAGE, code));
        }
    }

    public static void validate(String name, String code, String sign) {
        code = code.toUpperCase().trim();
        if      ((code == null || !code.matches(ISO_4217)) ||
                (name == null || !name.matches(FULL_NAME)) ||
                (sign == null || !sign.matches(SIGN))){
            throw new ValidationException(String.format(INVALID_CURRENCY_FIELDS_MESSAGE, code, name, sign));
        }
    }

    public static void validate(String baseCode, String targetCode){
        baseCode = baseCode.toUpperCase().trim();
        targetCode = targetCode.toUpperCase().trim();
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217))){
            throw new ValidationException(String.format(INVALID_EXCHANGE_INPUT_MESSAGE, baseCode, targetCode));
        }
    }

    public static void validate(String baseCode, String targetCode, BigDecimal rate){
        baseCode.toUpperCase().trim();
        targetCode.toUpperCase().trim();
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217)) ||
                (rate.compareTo(BigDecimal.ZERO) <= 0)){
            throw new ValidationException(String.format(INVALID_EXCHANGE_RATE_INPUT_MESSAGE, baseCode, targetCode, rate));
        }
    }

    public static void pathInfoValidate(String pathInfo) {
        if(pathInfo == null || pathInfo.length() != 7){
            throw new ValidationException(String.format(INVALID_PATH_INFO_MESSAGE, pathInfo));
        }
    }

    public static boolean entityAlreadyValidation (SQLException e) {
        return e.getErrorCode() == SQLITE_ERROR_CODE &&
                e.getMessage() != null &&
                e.getMessage().contains(SQLITE_ERROR_MESSAGE);
    }
}