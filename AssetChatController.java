package com.enterprise.ai.gateway.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetChatController {

    private final ChatClient chatClient;

    // Dependency Injection: Spring automatically hands the global robot guard bean to our mailbox!
    public AssetChatController(ChatClient enterpriseChatClient) {
        this.chatClient = enterpriseChatClient;
    }

    @GetMapping("/api/v1/chat")
    public String initiateSecureChat(@RequestParam(value = "prompt") String prompt) {
        // Hand the prompt to the robot guard and wait for the filtered response payload
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}