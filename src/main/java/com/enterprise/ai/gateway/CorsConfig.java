// Subsystem Name: Network Boundary & CORS Configuration Layer
// Domain Context: Ingress Boundary Control Group
// File Location: src/main/java/com/enterprise/ai/gateway/CorsConfig.java

package com.enterprise.ai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // CRITICAL: Execute before EnterpriseSecurityFilter checks for tokens
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow recruiters to easily pass data across origin spaces
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Supports wildcard handshakes for cross-origin local viewports
        config.addAllowedHeader("*");        // Accepts any custom client authorization strings
        config.addAllowedMethod("*");        // Grants access for GET, POST, and preflight OPTIONS packets
        
        // Bind the passport treaty rules strictly to our AI API context paths
        source.registerCorsConfiguration("/api/v1/**", config);
        
        return new CorsFilter(source);
    }
}