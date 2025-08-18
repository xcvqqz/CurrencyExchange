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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class CurrencyServlet extends HttpServlet {


    private final CurrencyService currencyService = new CurrencyService(new CurrencyDao());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        String code = path.substring(1);

        if (code.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Currency code parameter is missing");
        }

        try {
            Optional<CurrencyDto> currencyOpt = currencyService.findByCode(code);

            if (currencyOpt.isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currencyOpt.get());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(response.getWriter(), Map.of("error", "Currency with code '" + code + "' not found"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Логируем ошибку (если есть логгер)
            // logger.error("Database error while fetching currency: " + code, e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(response.getWriter(), Map.of("error", "Internal server error"));
        }
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
            mapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), currency);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
