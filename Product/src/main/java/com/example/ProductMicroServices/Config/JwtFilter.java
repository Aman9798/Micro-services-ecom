package com.example.ProductMicroServices.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

       // System.out.println(requestURI);

        if (("/products/".equals(requestURI) && "POST".equalsIgnoreCase(method)) ||
                (requestURI.matches("/products/\\d+") && "PATCH".equalsIgnoreCase(method)) ||
                (requestURI.matches("/products/\\d+") && "DELETE".equalsIgnoreCase(method))) {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
