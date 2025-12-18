package com.sunsettle.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * TEMPORARY DISABLED FOR MVP / CLIENT DEMO
 *
 * This filter is intentionally bypassed so that:
 * - No JWT is required
 * - No Authentication is set
 * - No 401 / 403 errors
 *
 * We will re-enable this AFTER client validation.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 🔓 DO NOTHING, JUST CONTINUE THE CHAIN
        filterChain.doFilter(request, response);
    }
}
