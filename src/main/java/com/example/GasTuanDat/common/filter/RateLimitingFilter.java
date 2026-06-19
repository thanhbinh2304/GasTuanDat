package com.example.GasTuanDat.common.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements Filter {

    // Cache to store the number of requests per IP. TTL = 15 minutes.
    private final Cache<String, Integer> requestCounts = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        // Only apply rate limiting to authentication endpoints
        if (uri.contains("/auth/login") || uri.contains("/auth/forgot-password") || uri.contains("/auth/reset-password")) {
            String clientIp = getClientIP(httpRequest);
            String key = clientIp + "-" + uri;

            Integer count = requestCounts.getIfPresent(key);
            if (count == null) {
                count = 0;
            }

            if (count >= 5) { // Maximum 5 requests per 15 minutes
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Too many requests. Please try again after 15 minutes.\"}");
                return;
            }

            requestCounts.put(key, count + 1);
        }

        chain.doFilter(request, response);
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
