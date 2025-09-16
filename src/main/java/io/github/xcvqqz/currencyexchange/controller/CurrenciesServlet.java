package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;
import java.util.List;

import io.github.xcvqqz.currencyexchange.dto.CurrencyRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class CurrenciesServlet extends BasicServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<CurrencyResponseDTO> currencyDtosResponse = currencyService.findAll();
        doResponse(response, SC_OK, currencyDtosResponse);
    }

    @Override
    public  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        Validator.validate(name, code, sign);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(name, code, sign);
        Currency currency = currencyMapper.convertToCurrency(currencyRequestDTO);
        CurrencyResponseDTO currencyDtoResponse = currencyMapper.convertToDto(currencyService.save(currency.getName(), currency.getCode(), currency.getSign()));

        doResponse(response, SC_OK, currencyDtoResponse);
    }
}