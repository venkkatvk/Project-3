// Subsystem Name: Ingress Orchestration & Routing Layer
// Domain Context: Boundary Ingress Execution Group
// File Location: src/main/java/com/enterprise/ai/gateway/AssetChatController.java

package com.enterprise.ai.gateway;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AssetChatController {

    private static final double CACHE_SIMILARITY_THRESHOLD = 0.55;

    private final VectorStore vectorStore;
    private final ModelProtectionService modelProtectionService;

    public AssetChatController(VectorStore vectorStore, ModelProtectionService modelProtectionService) {
        this.vectorStore = vectorStore;
        this.modelProtectionService = modelProtectionService;
    }

    @GetMapping("/chat")
    public String routeIngressSecureChatGet(@RequestParam("prompt") String userPrompt) {
        return routePrompt(userPrompt);
    }

    @PostMapping("/chat")
    public String routeIngressSecureChat(@RequestBody Map<String, String> payload) {
        return routePrompt(payload.get("message"));
    }

    private String routePrompt(String userPrompt) {
        System.out.println("📡 [INGRESS REACHED] Boundary portal processing prompt token streams...");

        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return "{\"error\": \"Prompt payload content string is hollow.\"}";
        }

        try {
            List<Document> matchedCachedResults = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(userPrompt)
                            .topK(1)
                            .similarityThreshold(CACHE_SIMILARITY_THRESHOLD)
                            .build()
            );
            if (!matchedCachedResults.isEmpty()) {
                System.out.println("🟩 [SEMANTIC CACHE HIT] Matching vector coordinates resolved in Redis vault!");
                return "[VECTOR CACHE HIT] " + matchedCachedResults.get(0).getText();
            }
        } catch (Exception cacheException) {
            System.err.println("⚠️ [CACHE DEGRADATION] Vault search skipped: " + cacheException.getMessage());
        }

        System.out.println("🔀 [SEMANTIC CACHE MISS] Forwarding prompt directly to fault-tolerance shield...");
        return modelProtectionService.executeProtectedModelInference(userPrompt);
    }
}
