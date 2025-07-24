package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;

import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


public class CurrencyServlet extends HttpServlet {


    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        String code = path.substring(1);
        PrintWriter printWriter = response.getWriter();
        Currency currency;

        if (code == null || code.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.println("Currency code parameter is missing");
        }

        try {
            try {
                currency = currencyService.findByCode(code);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            printWriter.println("<html>");
            printWriter.println("<h1> " + currency.getId() + "  |  " + currency.getCode() + "  |  " + currency.getFullName() + "  |  " + currency.getSign() + " <h1>");
            printWriter.println("<html>");

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");


        try (PrintWriter printWriter = response.getWriter()){
            String code = request.getParameter("code");
            String fullName = request.getParameter("fullName");
            String sign = request.getParameter("sign");
            int id = Integer.parseInt(request.getParameter("id"));

            Currency currency = new Currency(id,code,fullName,sign);

            currencyService.updateCurrency(currency);

            printWriter.println("<html>");
            printWriter.println("<head><title>Success</title></head>");
            printWriter.println("<body>");
            printWriter.println("<h1>УСПЕХ</h1>");
            printWriter.println("<p>Update - " +  currency.getFullName() + " Успешен</p>");
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
