package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDto;
import io.github.xcvqqz.currencyexchange.service.ExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeServlet extends BasicServlet {

    private final ExchangeService  exchangeService = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));

        ExchangeResponseDto exchangeResponseDto = exchangeService.convert(from, to, amount);
        doResponse(response, SC_OK, exchangeResponseDto);

    }
}
