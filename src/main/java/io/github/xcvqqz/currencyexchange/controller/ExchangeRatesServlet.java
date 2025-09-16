package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeRatesServlet extends BasicServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ExchangeRateResponseDTO> exchangeRateDtoResponse = exchangeRatesService.findAll();
        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");
        BigDecimal rate = new BigDecimal(request.getParameter("rate"));

        Validator.validate(baseCode, targetCode, rate);

        ExchangeRateRequestDTO exchangeRateRequestDto = new ExchangeRateRequestDTO(baseCode, targetCode, rate);
        ExchangeRate exchangeRate = exchangeRateMapper.convertToExchangeRate(exchangeRateRequestDto);
        ExchangeRateResponseDTO exchangeRateDtoResponse = exchangeRateMapper.convertToDto(exchangeRatesService.save(exchangeRate));

        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }
}