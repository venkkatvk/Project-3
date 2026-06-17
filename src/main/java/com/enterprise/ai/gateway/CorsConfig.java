// Subsystem Name: Cross-Origin Resource Gateway Layer
// Domain Context: Boundary Security Infrastructure Group
// File Location: src/main/java/com/enterprise/ai/gateway/CorsConfig.java

package com.enterprise.ai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Explicitly welcoming your Vite development browser workspace origin
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        
        // Binding this security passport template across all incoming API corridors
        source.registerCorsConfiguration("/api/v1/**", config);
        return new CorsFilter(source);
    }
}