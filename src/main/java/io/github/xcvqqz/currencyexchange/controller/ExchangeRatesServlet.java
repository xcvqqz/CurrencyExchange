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

public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        List<ExchangeRateDto> exchangeRates;

        try {
            exchangeRates = exchangeRatesService.getAllExchangeRates();
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRates);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        ExchangeRateDto exchangeRates;


        String baseCode = request.getParameter("baseCode");
        String targetCode = request.getParameter("targetCode");
        double rate = Double.parseDouble(request.getParameter("rate"));


        try {
            exchangeRates = exchangeRatesService.createExchangeRates(baseCode, targetCode, rate);
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRates);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}