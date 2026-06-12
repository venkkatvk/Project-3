package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiGatewayConfig {

    @Bean
    public ChatClient enterpriseChatClient(ChatClient.Builder builder, VectorStore vectorStore) {
        // Snapping our custom guard robot directly onto the client's default interception line
        return builder
                .defaultAdvisors(new SecurityPassAdvisor(vectorStore))
                .build();
    }
}