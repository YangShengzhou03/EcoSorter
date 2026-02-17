package com.ecosorter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTR = "startTime";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTR, Instant.now());
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Instant startTime = (Instant) request.getAttribute(START_TIME_ATTR);
        if (startTime != null) {
            long duration = Duration.between(startTime, Instant.now()).toMillis();
        }
    }
}
