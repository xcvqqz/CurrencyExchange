package io.github.xcvqqz.currencyexchange.filter;

import jakarta.servlet.*;

import java.io.IOException;

    public class ContentAndEncodingFilter implements Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

            servletRequest.setCharacterEncoding("UTF-8");
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json");

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


