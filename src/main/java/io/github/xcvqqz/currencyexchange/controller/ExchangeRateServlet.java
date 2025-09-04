package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRateResponseDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRateService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeRateServlet extends BasicServlet {


    private final ExchangeRateService exchangeRatesService = new ExchangeRateService();

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

        String baseCode = pathInfo.substring(1, 4);
        String targetCode = pathInfo.substring(4);
        Validator.validate(baseCode, targetCode);

        ExchangeRateResponseDto exchangeRateDtoResponse = exchangeRatesService.getExchangeRatesPair(baseCode, targetCode);

        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }


    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        Validator.pathInfoValidate(path);

        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();
        BigDecimal rate = new BigDecimal(request.getReader().readLine().substring(5));

        Validator.validate(baseCode, targetCode, rate);

        ExchangeRateResponseDto exchangeRateDtoResponse = exchangeRatesService.update(baseCode, targetCode, rate);

        doResponse(response, SC_OK, exchangeRateDtoResponse);
    }
}
