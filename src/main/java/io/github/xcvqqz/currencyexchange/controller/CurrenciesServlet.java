package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        List<Currency> currencies;

        try {
            currencies = currencyService.getAllCurrencies();
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currencies);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        Currency currency;

            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");

            if (code == null || code.isEmpty() ||
                    fullName == null || fullName.isEmpty() ||
                    sign == null || sign.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing required parameters: code, name or sign");
                response.setStatus(500);
            }

        try {
            currency = currencyService.createCurrency(code, fullName, sign);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
        }
    }

