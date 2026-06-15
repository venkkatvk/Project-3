package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Enterprise Bounded Context: Conversational Ingress Gateway Layer
 * Refactored to handle JSON-wrapped payloads via HTTP POST.
 */
@RestController
public class AssetChatController {

    private final ChatClient chatClient;

    public AssetChatController(ChatClient enterpriseChatClient) {
        this.chatClient = enterpriseChatClient;
    }

    public record ChatRequest(String message) {}

    @PostMapping("/api/v1/chat")
    public String initiateSecureChat(@RequestBody ChatRequest request) {
        return this.chatClient.prompt()
                .user(request.message())
                .call()
                .content();
    }
}