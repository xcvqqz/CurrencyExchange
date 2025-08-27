package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;



public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        List<CurrencyDto> currencies;

        try {
            currencies = currencyService.getAllCurrencies();
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currencies);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public  void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        CurrencyDto currency;

            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");

          Validator.validate(code, fullName, sign);

        try {
            currency = currencyService.createCurrency(code, fullName, sign);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
        }
    }

