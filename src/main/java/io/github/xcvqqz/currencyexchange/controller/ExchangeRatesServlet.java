package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeRatesServlet extends BasicServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ExchangeRateDto> exchangeRateDtosResponse = exchangeRatesService.findAll();
        doResponse(response, SC_OK, exchangeRateDtosResponse);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String baseCode = request.getParameter("baseCode");
        String targetCode = request.getParameter("targetCode");
        double rate = Double.parseDouble(request.getParameter("rate"));
        Validator.validate(baseCode, targetCode, rate);

        ExchangeRateDto exchangeRateDtosResponse = exchangeRatesService.save(baseCode, targetCode, rate);
        doResponse(response, SC_OK, exchangeRateDtosResponse);

    }
}