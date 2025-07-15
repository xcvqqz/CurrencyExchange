package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.util.List;

import io.github.xcvqqz.currencyexchange.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class GetAllCurrenciesServlet extends HttpServlet {


    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter printWriter = response.getWriter();
        List<Currency> currency;


        try {
            currency = currencyService.getAllCurrencies();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        printWriter.println("<html>");
        for(Currency c : currency) {
            printWriter.println("<h1> " + c.getId() + "  |  " + c.getCode() + "  |  " + c.getFullName() + "  |  " + c.getSign() + " <h1>");
        }
        printWriter.println("<html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}

