package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateServlet extends HttpServlet {


    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService(new ExchangeRatesDao());
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String baseCode = request.getParameter("baseCode");
        String targetCode = request.getParameter("targetCode");

        if (baseCode == null || targetCode == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing parameters: baseCode and targetCode are required\"}");
            return;
        }

        try {
            ExchangeRates exchangeRate = exchangeRatesService.getExchangeRates(baseCode, targetCode);
            if (exchangeRate == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Exchange rate not found for " + baseCode + " to " + targetCode + "\"}");
            } else {
                mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate);
            }
        } catch (ClassNotFoundException | SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}
