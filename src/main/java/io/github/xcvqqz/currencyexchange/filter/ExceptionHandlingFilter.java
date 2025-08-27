//package io.github.xcvqqz.currencyexchange.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.Map;
//
//@WebFilter("/*")
//public class ExceptionHandlingFilter implements Filter {
//
//    ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//    }
//
//    protected void sendError(HttpServletResponse response, String message, int status) throws IOException {
//        response.setStatus(status);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(mapper.writeValueAsString(Map.of("error", message)));
//    }
//}