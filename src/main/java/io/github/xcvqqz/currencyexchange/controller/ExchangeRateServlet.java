package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static io.github.xcvqqz.currencyexchange.util.Validator.isValid;

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

        Optional<ExchangeRatesDto> exchangeRate = Optional.empty();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() != 7) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid URL format. Use /exchangeRate/EURRUB\"}");
            return;
        }

        String baseCode = pathInfo.substring(1, 4).toUpperCase();
        String targetCode = pathInfo.substring(4).toUpperCase();


        if (!isValid(baseCode, targetCode)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Currency codes must be 3 uppercase letters\"}");
            return;
        }


        try {
            exchangeRate = exchangeRatesService.getExchangeRates(baseCode, targetCode);
            if (exchangeRate.isEmpty()) {
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
        ExchangeRatesDto exchangeRate;

        if (path == null || path.length() < 7) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();
        Double rate = Double.parseDouble(request.getReader().readLine().substring(5));


        if (!isValid(baseCode,targetCode,rate)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Incorrect baseCode, targetCode, or rate value entered.");
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
