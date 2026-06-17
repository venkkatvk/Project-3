package com.enterprise.ai.gateway;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Isolated Bounded Context: Stateless Boundary Security Subsystem
 * Decoupled interceptor filter processing incoming cryptographic authentication headers.
 */
@Component
public class EnterpriseSecurityFilter implements Filter {

    private static final String EXPECTED_TOKEN = "secret-virtual-thread-token-2026";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // CRITICAL FIX: Explicitly bypass the security check for HTTP OPTIONS preflight handshake
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return; // Terminate filter evaluation early for safe preflight clearance
        }

        // Perimeter Auth Logic for standard data transactions (POST/GET)
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.equals("Bearer " + EXPECTED_TOKEN)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"EDGE REJECTION: Invalid Perimeter Token\"}");
            return;
        }

        // Handing off transaction focus to downstream endpoints if credential checks clear
        chain.doFilter(request, response);
    }
}