package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.entity.ExchangeRates;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateServlet extends HttpServlet {


    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService(new ExchangeRatesDao());
    private final ObjectMapper mapper = new ObjectMapper();



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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExchangeRatesDto exchangeRate = null;
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() != 7) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid URL format. Use /exchangeRate/EURRUB\"}");
            return;
        }

        String baseCode = pathInfo.substring(1, 4).toUpperCase();
        String targetCode = pathInfo.substring(4).toUpperCase();


        if (!baseCode.matches("[A-Z]{3}") || !targetCode.matches("[A-Z]{3}")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Currency codes must be 3 uppercase letters\"}");
            return;
        }

        if (baseCode == null || targetCode == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing parameters: baseCode and targetCode are required\"}");
            return;
        }

        try {
            exchangeRate = exchangeRatesService.getExchangeRates(baseCode, targetCode);
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


    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        ExchangeRatesDto exchangeRate = null;

        if (path == null || path.length() < 7) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();


        if (!baseCode.matches("[A-Z]{3}") || !targetCode.matches("[A-Z]{3}")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


        String rateParam = request.getReader().readLine().substring(5);
        if (rateParam == null || rateParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Параметр 'rate' обязателен");
            return;
        }

        double rate;
        try {
            rate = Double.parseDouble(rateParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Параметр 'rate' должен быть числом");
            return;
        }

        if (rate < 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Курс не может быть отрицательным");
            return;
        }

            try {
                 exchangeRate = exchangeRatesService.updateExchangeRates(baseCode, targetCode, rate);
                mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate);
            } catch (SQLException | ClassNotFoundException | IOException | RuntimeException e) {
                throw new RuntimeException(e);
            }
        } 
    }
