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


public class CurrencyServlet extends HttpServlet {


    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();
        String code = path.substring(1);

        Validator.validate(code);

        CurrencyDto currency = currencyService.findByCode(code);
        mapper.writeValue(response.getWriter(), Map.of("error", "Internal server error"));

    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CurrencyDto currency;

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");
        int id = Integer.parseInt(request.getParameter("id"));

        Validator.validate(code, fullName, sign);

        currency = currencyService.update(new Currency(id, code, fullName, sign));
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
    }
}
