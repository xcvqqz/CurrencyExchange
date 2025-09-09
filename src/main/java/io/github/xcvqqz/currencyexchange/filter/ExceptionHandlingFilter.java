package io.github.xcvqqz.currencyexchange.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.dto.ErrorResponseDto;
import io.github.xcvqqz.currencyexchange.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


import static jakarta.servlet.http.HttpServletResponse.*;

public class ExceptionHandlingFilter implements Filter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try {
            filterChain.doFilter(httpRequest, httpResponse);
        } catch (CurrencyNotFoundException e){
            sendError(httpResponse, e, SC_NOT_FOUND);
        }
        catch (DataBaseException e){
            sendError(httpResponse, e, SC_INTERNAL_SERVER_ERROR);
        }
        catch (EntityAlreadyExistException e){
            sendError(httpResponse, e, SC_CONFLICT);
        }
        catch (ExchangeRateNotFoundException e){
            sendError(httpResponse, e, SC_NOT_FOUND);
        }
        catch(ValidationException e){
            sendError(httpResponse, e, SC_BAD_REQUEST);
        }
    }

    private void sendError(HttpServletResponse response, Exception e, int status) throws IOException {
        response.setStatus(status);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        response.getWriter().write(mapper.writeValueAsString(errorResponseDto));
    }
}