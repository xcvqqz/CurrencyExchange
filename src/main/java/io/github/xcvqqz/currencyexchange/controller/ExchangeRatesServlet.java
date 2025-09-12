package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRateService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeRatesServlet extends BasicServlet {

    private final ExchangeRateService exchangeRatesService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ExchangeRateResponseDto> exchangeRateDtosResponse = exchangeRatesService.findAll();
        doResponse(response, SC_OK, exchangeRateDtosResponse);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");
        BigDecimal rate = new BigDecimal(request.getParameter("rate"));

        Validator.validate(baseCode, targetCode, rate);

        ExchangeRateResponseDto exchangeRateDtosResponse = exchangeRatesService.save(baseCode, targetCode, rate);
        doResponse(response, SC_OK, exchangeRateDtosResponse);
    }
}