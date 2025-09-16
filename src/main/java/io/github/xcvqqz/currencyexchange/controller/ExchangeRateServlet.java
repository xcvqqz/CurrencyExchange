package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRate;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeRateServlet extends BasicServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pathInfo = request.getPathInfo();
        Validator.pathInfoValidate(pathInfo);

        String baseCode = pathInfo.substring(FROM, TO);
        String targetCode = pathInfo.substring(TO);
        Validator.validate(baseCode, targetCode);


        ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO(baseCode, targetCode, BigDecimal.ZERO);
        ExchangeRateResponseDTO exchangeRateDtoResponse = exchangeRateMapper.convertToDto(
                exchangeRatesService.getExchangeRatesPair(
                exchangeRateRequestDTO.baseCurrencyCode(),
                exchangeRateRequestDTO.targetCurrencyCode()));

        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }

    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        Validator.pathInfoValidate(path);

        String baseCode = path.substring(FROM, TO).toUpperCase();
        String targetCode = path.substring(TO).toUpperCase();
        BigDecimal rate = new BigDecimal(request.getReader().readLine().substring(RATE_POSITION));

        Validator.validate(baseCode, targetCode, rate);

        ExchangeRateRequestDTO exchangeRateRequestDto = new ExchangeRateRequestDTO(baseCode, targetCode, rate);
        ExchangeRate exchangeRate = exchangeRateMapper.convertToExchangeRate(exchangeRateRequestDto);
        ExchangeRateResponseDTO exchangeRateDtoResponse = exchangeRateMapper.convertToDto(exchangeRatesService.update(exchangeRate));

        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }
}