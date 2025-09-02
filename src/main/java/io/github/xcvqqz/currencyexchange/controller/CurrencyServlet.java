package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;


public class CurrencyServlet extends BasicServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        String code = path.substring(1);
        Validator.validate(code);

        CurrencyDto currencyDtoResponse = currencyService.findByCode(code);
        doResponse(response, SC_OK, currencyDtoResponse);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");
        int id = Integer.parseInt(request.getParameter("id"));

        Validator.validate(code, fullName, sign);

        CurrencyDto currencyDtoResponse = currencyService.update(new Currency(id, code, fullName, sign));
        doResponse(response, SC_OK, currencyDtoResponse);
    }
}
