package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;

import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;


public class CurrencyServlet extends BasicServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        String code = path.substring(1);
        Validator.validate(code);

        CurrencyResponseDto currencyDtoResponse = currencyService.findByCode(code);
        doResponse(response, SC_OK, currencyDtoResponse);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");
        int id = Integer.parseInt(request.getParameter("id"));

//        Validator.validate(code, name, sign);

        CurrencyResponseDto currencyDtoResponse = currencyService.update(new Currency(id, code, name, sign));
        doResponse(response, SC_OK, currencyDtoResponse);
    }
}
