package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeRateServlet extends BasicServlet {


    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        Validator.pathInfoValidate(pathInfo);

        String baseCode = pathInfo.substring(1, 4);
        String targetCode = pathInfo.substring(4);

        Validator.validate(baseCode, targetCode);

        Optional<ExchangeRateDto> exchangeRate = exchangeRatesService.getExchangeRatesPair(baseCode, targetCode);
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate);
    }


    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        ExchangeRateDto exchangeRate;

        Validator.pathInfoValidate(path);

        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();
        Double rate = Double.parseDouble(request.getReader().readLine().substring(5));

        Validator.validate(baseCode, targetCode, rate);

        exchangeRate = exchangeRatesService.update(baseCode, targetCode, rate);
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate);

        } 
    }
