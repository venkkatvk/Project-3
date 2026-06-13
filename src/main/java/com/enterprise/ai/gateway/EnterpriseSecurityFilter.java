package com.enterprise.ai.gateway;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Isolated Bounded Context: Stateless Boundary Security Subsystem
 * Decoupled interceptor filter processing incoming cryptographic authentication headers.
 */
public class EnterpriseSecurityFilter implements Filter {

    // Simulating an enterprise HMAC signing key token parameter for validation passes
    private static final String SACRED_GATEWAY_TOKEN = "Bearer secret-virtual-thread-token-2026";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");

        // Cryptographic Perimeter Assertion Loop
        if (authHeader == null || !authHeader.equals(SACRED_GATEWAY_TOKEN)) {
            // Short-circuiting the request thread line before it reaches inner business controllers
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"security_error\": \"Missing or invalid cryptographic signature token pass.\"}");
            return;
        }

        // Token signature verified cleanly. Advance the execution handle down the filter chain pipeline.
        chain.doFilter(request, response);
    }
}