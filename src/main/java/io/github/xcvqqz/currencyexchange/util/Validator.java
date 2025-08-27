package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.exception.ValidationException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.regex.Pattern;

public class Validator {

    private Validator(){}

    private static final String ISO_4217 ="^[A-Z]{3}$";
    private static final String FULL_NAME = "^[\\p{L} .'-]{1,30}$";
    private static final String SIGN = "^[\\p{Sc}\\p{So}]{1,3}$";
    private static final String INVALID_CODE_MESSAGE = "Invalid code = %s entered.";
    private static final String INVALID_EXCHANGE_RATE_INPUT_MESSAGE = "Incorrect baseCode = %s, targetCode = %s, or rate = %f value entered.";
    private static final String INVALID_EXCHANGE_INPUT_MESSAGE = "Incorrect baseCode = %s or targetCode = %s value entered.";
    private static final String INVALID_CURRENCY_FIELDS_MESSAGE = "Incorrect code = %s, fullName = %s, or sign = %s value entered.";
    private static final String INVALID_PATH_INFO_MESSAGE = "Invalid URL format = %s";


    public static void validate(String code) {
        code.toUpperCase().trim();
        if (code == null || !code.matches(ISO_4217)) {
            throw new ValidationException(String.format(INVALID_CODE_MESSAGE, code));
        }
    }

    public static void validate(String code, String fullName, String sign) {
        code.toUpperCase().trim();
        if      ((code == null || !code.matches(ISO_4217)) ||
                (fullName == null || !fullName.matches(FULL_NAME)) ||
                (sign == null || !sign.matches(SIGN))){
            throw new ValidationException(String.format(INVALID_CURRENCY_FIELDS_MESSAGE, code, fullName, sign));
        }
    }

    public static void validate(String baseCode, String targetCode){
        baseCode.toUpperCase().trim();
        targetCode.toUpperCase().trim();
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217))){
            throw new ValidationException(String.format(INVALID_EXCHANGE_INPUT_MESSAGE, baseCode, targetCode));
        }
    }

    public static void validate(String baseCode, String targetCode, double rate){
        baseCode.toUpperCase().trim();
        targetCode.toUpperCase().trim();
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217)) ||
                (rate < 0)){
            throw new ValidationException(String.format(INVALID_EXCHANGE_RATE_INPUT_MESSAGE, baseCode, targetCode, rate));
        }
    }

    public static void pathInfoValidate(String pathInfo) {
            if(pathInfo == null || pathInfo.length() != 7){
                throw new ValidationException(String.format(INVALID_PATH_INFO_MESSAGE, pathInfo));
            }
    }

}
