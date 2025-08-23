package io.github.xcvqqz.currencyexchange.util;

import io.github.xcvqqz.currencyexchange.exception.ValidationException;

import java.util.regex.Pattern;

public class Validator {

    private Validator(){}

    private static final String ISO_4217 ="^[A-Z]{3}$";
    private static final String FULL_NAME = "^[\\p{L} .'-]{1,30}$";
    private static final String SIGN = "^[\\p{Sc}\\p{So}]{1,3}$";

    public static void isValid(String code) {
        if (code == null || !code.matches(ISO_4217)) {
            throw new ValidationException("Invalid code entered");
        }
    }

    public static void isValid(String code, String fullName, String sign) {
        if      ((code == null || !code.matches(ISO_4217)) ||
                (fullName == null || !fullName.matches(FULL_NAME)) ||
                (sign == null || !sign.matches(SIGN))){
            throw new ValidationException("Incorrect code, fullName, or sign value entered.");
        }
    }

    public static void isValid(String baseCode, String targetCode){
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217))){
            throw new ValidationException("Incorrect baseCode or targetCode value entered.");
        }
    }

    public static void isValid(String baseCode, String targetCode, double rate){
        if      ((baseCode == null || !baseCode.matches(ISO_4217)) ||
                (targetCode == null || !targetCode.matches(ISO_4217)) ||
                (rate < 0)){
            throw new ValidationException("Incorrect baseCode, targetCode, or rate value entered.");
        }
    }
}
