package com.uit.petrescueapi.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * Logs every HTTP request and its response status + latency.
 * Skips health-check and actuator endpoints to reduce noise.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (shouldSkip(request)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingResponseWrapper wrapped = new ContentCachingResponseWrapper(response);
        long start = System.currentTimeMillis();

        try {
            log.info("==> {} {}", request.getMethod(), request.getRequestURI());
            chain.doFilter(request, wrapped);
        } finally {
            long ms = System.currentTimeMillis() - start;
            log.info("<== {} {} | {} | {}ms",
                    request.getMethod(), request.getRequestURI(), wrapped.getStatus(), ms);
            wrapped.copyBodyToResponse();
        }
    }

    private boolean shouldSkip(HttpServletRequest r) {
        String p = r.getRequestURI();
        return p.contains("/actuator") || p.contains("/swagger") || p.contains("/api-docs")
                || p.equals("/favicon.ico");
    }
}
