package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRateDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesServlet extends BasicServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<ExchangeRateDto> exchangeRates = exchangeRatesService.findAll();
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRates);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        ExchangeRateDto exchangeRates;

        String baseCode = request.getParameter("baseCode");
        String targetCode = request.getParameter("targetCode");
        double rate = Double.parseDouble(request.getParameter("rate"));


        exchangeRates = exchangeRatesService.save(baseCode, targetCode, rate);
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRates);

    }
}