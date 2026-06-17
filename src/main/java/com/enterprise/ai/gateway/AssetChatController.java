// Subsystem Name: Ingress Orchestration & Routing Layer
// Domain Context: Boundary Ingress Execution Group
// File Location: src/main/java/com/enterprise/ai/gateway/AssetChatController.java

package com.enterprise.ai.gateway;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AssetChatController {

    private final VectorStore vectorStore;
    private final ModelProtectionService modelProtectionService;

    // Dual structural dependency injection linking cache space and resilience blocks
    public AssetChatController(VectorStore vectorStore, ModelProtectionService modelProtectionService) {
        this.vectorStore = vectorStore;
        this.modelProtectionService = modelProtectionService;
    }

    @PostMapping("/chat")
    public String routeIngressSecureChat(@RequestBody Map<String, String> payload) {
        String userPrompt = payload.get("message");
        System.out.println("📡 [INGRESS REACHED] Boundary portal processing prompt token streams...");

        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return "{\"error\": \"Prompt payload content string is hollow.\"}";
        }

        // STEP 1: High-Speed Semantic Vector Cache Lookup
        try {
            List<Document> matchedCachedResults = vectorStore.similaritySearch(userPrompt);
            if (!matchedCachedResults.isEmpty()) {
                System.out.println("🟩 [SEMANTIC CACHE HIT] Matching vector coordinates resolved in Redis vault!");
                return "[VECTOR CACHE HIT] " + matchedCachedResults.get(0).getText();
            }
        } catch (Exception cacheException) {
            System.err.println("⚠️ [CACHE DEGRADATION] Vault search skipped: " + cacheException.getMessage());
        }

        // STEP 2: Circuit-Protected Upstream AI Model Execution
        System.out.println("🔀 [SEMANTIC CACHE MISS] Forwarding prompt directly to fault-tolerance shield...");
        return modelProtectionService.executeProtectedModelInference(userPrompt);
    }
}