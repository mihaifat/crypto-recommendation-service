package com.crypto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SupportedCryptoInterceptor implements HandlerInterceptor {

    @Value("${supported.crypto.currencies}")
    private List<String> supportedCryptoCurrencies;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String pathVariable = extractPathVariable(request.getRequestURI());
        boolean isSupportedCryptoCurrency = supportedCryptoCurrencies.contains(pathVariable);
        if (!isSupportedCryptoCurrency) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("The crypto currency " + pathVariable + " is not supported.");
            return false;
        }
        return true;
    }

    private String extractPathVariable(String requestURI) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(requestURI).build();
        return uriComponents.getPathSegments().get(1);
    }

}
