package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Enterprise Bounded Context: Conversational Ingress Gateway Layer
 * Exposes securely isolated, stateful REST API endpoints for user request interceptors.
 */
@RestController
public class AssetChatController {

    private final ChatClient chatClient;

    // Dependency Injection: Spring automatically wires our context-aware, advisor-augmented ChatClient
    public AssetChatController(ChatClient enterpriseChatClient) {
        this.chatClient = enterpriseChatClient;
    }

    /**
     * Data Transfer Object (DTO) Record
     * Encapsulates the inbound JSON serialization matrix frame cleanly.
     */
    public record ChatRequest(String message) {}

    /**
     * Intercepts incoming POST data payloads to route inquiries past our security advisors.
     */
    @PostMapping("/api/v1/chat")
    public String initiateSecureChat(@RequestBody ChatRequest request) {
        // Direct execution routing: extracts the unboxed text token from our payload record frame
        return this.chatClient.prompt()
                .user(request.message())
                .call()
                .content();
    }
}