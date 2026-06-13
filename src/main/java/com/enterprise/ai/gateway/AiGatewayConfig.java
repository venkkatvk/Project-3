package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiGatewayConfig {

    // One Single Bounded Context: Instantiating our isolated, resilient ChatClient interceptor framework
    @Bean
    public ChatClient enterpriseChatClient(ChatModel chatModel, VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                // Bind our custom resilient security pass interceptor into the execution pipeline
                .defaultAdvisors(new SecurityPassAdvisor(vectorStore))
                .build();
    }
}