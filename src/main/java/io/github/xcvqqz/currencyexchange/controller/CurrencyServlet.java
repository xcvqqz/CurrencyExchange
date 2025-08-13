package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dto.CurrencyDto;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


public class CurrencyServlet extends HttpServlet {


    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        String code = path.substring(1);

        if (code.isEmpty()) {
            response.setStatus(500);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Currency code parameter is missing");
        }

        CurrencyDto currency = null;
        try {
            currency = currencyService.findByCode(code);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        CurrencyDto currency;

        String code = request.getParameter("code");
        String fullName = request.getParameter("fullName");
        String sign = request.getParameter("sign");
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            currency = currencyService.updateCurrency(new Currency(id, code, fullName, sign));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
    }
}
