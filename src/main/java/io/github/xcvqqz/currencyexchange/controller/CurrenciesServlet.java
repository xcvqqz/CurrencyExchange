package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.dao.CurrencyDao;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("response.setContentType(application/json");
        response.setCharacterEncoding("UTF-8");
        List<Currency> currencies;

        try  {
            currencies = currencyService.getAllCurrencies();
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currencies);
        } catch (ClassNotFoundException e) {
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

