package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dao.ExchangeRatesDao;
import io.github.xcvqqz.currencyexchange.dto.ExchangeRatesDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeRatesService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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

        Validator.pathInfoValidate(pathInfo);

        String baseCode = pathInfo.substring(1, 4);
        String targetCode = pathInfo.substring(4);

        Validator.validate(baseCode, targetCode);

        try {
            exchangeRate = exchangeRatesService.getExchangeRates(baseCode, targetCode);
            if (exchangeRate.isPresent()) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate.get());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Exchange rate not found for " + baseCode + " to " + targetCode + "\"}");
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

        Validator.pathInfoValidate(path);

        String baseCode = path.substring(1, 4).toUpperCase();
        String targetCode = path.substring(4).toUpperCase();
        Double rate = Double.parseDouble(request.getReader().readLine().substring(5));

        Validator.validate(baseCode, targetCode, rate);

            try {
                 exchangeRate = exchangeRatesService.updateExchangeRates(baseCode, targetCode, rate);
                mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), exchangeRate);
            } catch (SQLException | ClassNotFoundException | IOException | RuntimeException e) {
                throw new RuntimeException(e);
            }
        } 
    }
