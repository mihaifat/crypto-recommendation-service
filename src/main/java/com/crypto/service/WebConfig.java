package com.crypto.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private SupportedCryptoInterceptor supportedCryptoInterceptor;
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(supportedCryptoInterceptor).addPathPatterns("/crypto/*/stats");
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
    }

}
