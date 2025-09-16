package io.github.xcvqqz.currencyexchange.controller;

import io.github.xcvqqz.currencyexchange.dto.ExchangeRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.ExchangeResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class ExchangeServlet extends BasicServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));

        ExchangeRequestDTO exchangeRequestDTO = new ExchangeRequestDTO(from, to, amount);
        ExchangeResponseDTO exchangeResponseDto = exchangeService.convert(exchangeRequestDTO);

        doResponse(response, SC_OK, exchangeResponseDto);
    }
}