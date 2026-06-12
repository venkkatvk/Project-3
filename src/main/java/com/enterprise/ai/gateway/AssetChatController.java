package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetChatController {

    private final ChatClient chatClient;

    // Dependency Injection: Spring automatically hands our custom configured ChatClient bean to this mailbox
    public AssetChatController(ChatClient enterpriseChatClient) {
        this.chatClient = enterpriseChatClient;
    }

    @GetMapping("/api/v1/chat")
    public String initiateSecureChat(@RequestParam(value = "prompt") String prompt) {
        // Direct execution routing: passes prompt down the advisor chain through the virtual guard
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
