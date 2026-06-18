// Subsystem Name: Self-Sustaining Telemetry Registry Layer
// Domain Context: Diagnostics & Telemetry Infrastructure Group
// File Location: src/main/java/com/enterprise/ai/gateway/ObservabilityConfig.java

package com.enterprise.ai.gateway;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    /**
     * Explicitly constructs an independent Standalone Meter Registry instance.
     * This ensures your architecture boots perfectly even when fat third-party 
     * platform instrumentation libraries are completely absent.
     */
    @Bean
    public MeterRegistry enterpriseMeterRegistry() {
        MeterRegistry standaloneRegistry = new SimpleMeterRegistry();
        
        // Injecting high-value global identification metadata markers
        standaloneRegistry.config().commonTags(
                "application", "healthcare-360-gateway",
                "environment", "production-us-east-1",
                "infrastructure", "virtual-thread-pool-citadel"
        );
        
        return standaloneRegistry;
    }

    /**
     * Binds our custom telemetry event tracker directly to the newly forged 
     * local context registry bean handle above.
     */
    @Bean
    public Counter initiateCacheHitTelemetryCounter(MeterRegistry enterpriseMeterRegistry) {
        return Counter.builder("gateway.cache.semantic.hits")
                .description("Tracks total successful text document coordinates resolved inside the Redis vault cluster")
                .register(enterpriseMeterRegistry);
    }
}