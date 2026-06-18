// Subsystem Name: Fault-Tolerant Circuit Breaker Subsystem
// Domain Context: Resilience & Ingress Protection Group
// File Location: src/main/java/com/enterprise/ai/gateway/ModelProtectionService.java

package com.enterprise.ai.gateway;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ModelProtectionService {

    private final ChatModel localMockChatModel;

    public ModelProtectionService(ChatModel localMockChatModel) {
        this.localMockChatModel = localMockChatModel;
    }

    @CircuitBreaker(name = "llmPerimeterShield", fallbackMethod = "triggerStaticEmergencyFallback")
    public String executeProtectedModelInference(String cleartextPrompt) {
        System.out.println("LOGGING: Routing payload through fault-tolerance filter.");
        
        if (cleartextPrompt.contains("trigger-error-simulation")) {
            throw new RuntimeException("Upstream AI Provider: 429 Too Many Requests");
        }
        
        return localMockChatModel.call(cleartextPrompt);
    }

    public String triggerStaticEmergencyFallback(String cleartextPrompt, Throwable throwable) {
        System.err.println("CIRCUIT TRIPPED. REASON: " + throwable.getMessage());
        
        return "⚠️ [ENTERPRISE FALLBACK] Upstream degradation intercepted by perimeter shield. Timestamp: " + LocalDateTime.now();
    }
}