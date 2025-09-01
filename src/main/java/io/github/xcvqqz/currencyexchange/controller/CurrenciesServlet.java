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

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<CurrencyDto> currencies;

        currencies = currencyService.findAll();
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currencies);
    }


    @Override
    public  void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CurrencyDto currency;

            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");

          Validator.validate(code, fullName, sign);

        currency = currencyService.save(code, fullName, sign);
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
        }
    }

