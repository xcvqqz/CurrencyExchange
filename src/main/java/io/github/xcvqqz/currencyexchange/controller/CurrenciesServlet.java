package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class CurrenciesServlet extends HttpServlet {


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
        for (Currency c : currency) {
            printWriter.println("<h1> " + c.getId() + "  |  " + c.getCode() + "  |  " + c.getFullName() + "  |  " + c.getSign() + " <h1>");
        }
        printWriter.println("<html>");
    }




    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter printWriter = response.getWriter()){
            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");

            if (code == null || code.isEmpty() ||
                    fullName == null || fullName.isEmpty() ||
                    sign == null || sign.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing required parameters: code, name or sign");
                return;
            }

            currencyService.createCurrency(code, fullName, sign);

            printWriter.println("<html>");
            printWriter.println("<head><title>Success</title></head>");
            printWriter.println("<body>");
            printWriter.println("<h1>УСПЕХ</h1>");
            printWriter.println("<p>Валюта " + code + " успешно добавлена</p>");
            printWriter.println("</body>");
            printWriter.println("</html>");

        } catch (SQLException | ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Database error: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request: " + e.getMessage());
        }
    }

}

