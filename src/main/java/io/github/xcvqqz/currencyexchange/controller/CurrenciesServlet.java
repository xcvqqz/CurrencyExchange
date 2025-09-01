package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.util.List;

import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class CurrenciesServlet extends BasicServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<CurrencyDto> currencyDtosResponse = currencyService.findAll();
        doResponse(response, SC_OK, currencyDtosResponse);

    }


    @Override
    public  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");

        Validator.validate(code, fullName, sign);

        CurrencyDto currencyDtoResponse = currencyService.save(code, fullName, sign);
        doResponse(response, SC_OK, currencyDtoResponse);
        }
    }

