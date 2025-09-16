package io.github.xcvqqz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.service.CurrencyService;
import io.github.xcvqqz.currencyexchange.service.ExchangeRateService;
import io.github.xcvqqz.currencyexchange.service.ExchangeService;
import io.github.xcvqqz.currencyexchange.util.mapper.CurrencyMapper;
import io.github.xcvqqz.currencyexchange.util.mapper.ExchangeRateMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BasicServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    protected final ExchangeRateService exchangeRatesService = new ExchangeRateService();
    protected final CurrencyService currencyService = new CurrencyService();
    protected final ExchangeRateMapper exchangeRateMapper = new ExchangeRateMapper();
    protected final CurrencyMapper currencyMapper = new CurrencyMapper();
    protected final ExchangeService exchangeService = new ExchangeService();
    protected static final int FROM = 1;
    protected static final int TO = 4;
    protected static final int RATE_POSITION = 5;

    protected void doResponse(HttpServletResponse response, int status, Object value) throws IOException {
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), value);
    }
}