package io.github.xcvqqz.currencyexchange.controller;

import java.io.*;

import io.github.xcvqqz.currencyexchange.dto.CurrencyRequestDTO;
import io.github.xcvqqz.currencyexchange.dto.CurrencyResponseDTO;
import io.github.xcvqqz.currencyexchange.entity.Currency;
import io.github.xcvqqz.currencyexchange.util.Validator;
import jakarta.servlet.http.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class CurrencyServlet extends BasicServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();
        String code = path.substring(FROM);
        Validator.validate(code);

        CurrencyResponseDTO currencyDtoResponse = currencyMapper.convertToDto(currencyService.findByCode(code));
        doResponse(response, SC_OK, currencyDtoResponse);
    }


//    This update method was not part of the technical specifications and was implemented on my own initiative.
//    Therefore, I unfortunately discovered the incomplete integration with the CurrencyRequestDTO very late in the process.
//    However, I decided against removing the method at that point.
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");
        int id = Integer.parseInt(request.getParameter("id"));

        Validator.validate(code, name, sign);

        CurrencyRequestDTO currencyRequestDTO = new CurrencyRequestDTO(name, code, sign);
        Currency currency = currencyMapper.convertToCurrency(currencyRequestDTO);
        CurrencyResponseDTO currencyDtoResponse = currencyMapper.convertToDto(currencyService.update(new Currency(id,
                currency.getName(),
                currency.getCode(),
                currency.getSign())));

        doResponse(response, SC_OK, currencyDtoResponse);
    }
}