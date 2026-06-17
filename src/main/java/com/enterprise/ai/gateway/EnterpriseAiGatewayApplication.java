
package com.enterprise.ai.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreAutoConfiguration;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;

/**
 * Core Orchestration Entry Point
 * Programmatically forces the exclusion of implicit framework data-store dependencies
 * to protect manual bean declarations from lifecycle hijacking.
 */
@SpringBootApplication(exclude = {
    RedisVectorStoreAutoConfiguration.class,
    PgVectorStoreAutoConfiguration.class
})
public class EnterpriseAiGatewayApplication {

    public static void main(String[] args) {
        // Igniting the high-throughput servlet engine context
        SpringApplication.run(EnterpriseAiGatewayApplication.class, args);
    }
}