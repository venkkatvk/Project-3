package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Enterprise Bounded Context: Conversational Ingress Gateway Layer
 * Handles incoming JSON-wrapped payload strings over secured HTTP POST channels.
 */
@RestController
public class AssetChatController {

    private final ChatClient chatClient;

    /**
     * Dependency Injection: The Spring IoC container automatically injects
     * our custom-configured, advisor-augmented ChatClient bean instance.
     */
    public AssetChatController(ChatClient enterpriseChatClient) {
        this.chatClient = enterpriseChatClient;
    }

    /**
     * Data Transfer Object (DTO) Record Definition
     * Maps the incoming JSON serialization schema keys cleanly to the heap space.
     */
    public record ChatRequest(String message) {}

    /**
     * Intercepts incoming POST data payloads to route inquiries past our security advisors.
     * Maps perfectly to incoming JSON keys matching the 'message' format matrix.
     */
    @PostMapping("/api/v1/chat")
    public String initiateSecureChat(@RequestBody ChatRequest request) {
        // Direct execution routing: extracts the raw text token string from the deserialized DTO frame
        return this.chatClient.prompt()
                .user(request.message())
                .call()
                .content();
    }
}