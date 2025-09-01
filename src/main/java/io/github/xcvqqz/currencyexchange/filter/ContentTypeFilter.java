package io.github.xcvqqz.currencyexchange.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

    @WebFilter("/*")
    public class ContentTypeFilter implements Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }


