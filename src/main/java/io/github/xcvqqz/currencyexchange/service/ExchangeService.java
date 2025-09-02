package io.github.xcvqqz.currencyexchange.service;

import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;

public class ExchangeService {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private static final String USD = "USD";


    public ExchangeRate convert(){

    }



    //прямое получение курса
    private ExchangeRate findDirectRate(String baseCode, String targetCode){

    }

    //получение обратного курса
    private ExchangeRate calculateInverseRate(String baseCode, String targetCode){

    }

    //получение кросс курса через USD
    private ExchangeRate calculateCrossRateViaUsd(String baseCode, String targetCode){

    }



}
