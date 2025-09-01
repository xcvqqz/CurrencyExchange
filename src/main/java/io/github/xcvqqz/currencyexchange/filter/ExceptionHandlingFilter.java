package io.github.xcvqqz.currencyexchange.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.currencyexchange.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try {
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
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
        response.getWriter().write(mapper.writeValueAsString(e.getMessage()));
    }
}