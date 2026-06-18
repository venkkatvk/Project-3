package com.enterprise.ai.gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Stateless Perimeter Guard
 * Validates ingress traffic at the servlet boundary before reaching AI orchestration layers.
 */
public class EnterpriseSecurityFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "X-Enterprise-Token";
    private static final String EXPECTED_TOKEN = "SIMULATED-SECURE-KEY-123"; // In prod, this would be a vault lookup

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        String token = request.getHeader(AUTH_HEADER);

        System.out.println("SECURITY SCAN: Intercepting request for " + requestPath);

        // Simple token validation logic for the stateless perimeter
        if (token == null || !token.equals(EXPECTED_TOKEN)) {
            System.err.println("SECURITY BREACH: Unauthorized access attempt rejected.");
            
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"UNAUTHORIZED\", \"message\": \"Missing or invalid Enterprise token.\"}");
            return;
        }

        // Token is valid, proceed to next filter or controller
        filterChain.doFilter(request, response);
    }
}