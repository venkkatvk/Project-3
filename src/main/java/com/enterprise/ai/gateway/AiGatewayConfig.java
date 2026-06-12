package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiGatewayConfig {

    @Bean
    public ChatClient enterpriseChatClient(ChatClient.Builder builder, VectorStore vectorStore) {
<<<<<<< HEAD
        // Snapping our custom guard robot directly onto the client's default interception line
=======
        // Programmatically binding our synchronized guard robot onto the default advice interceptor pipeline
>>>>>>> f367ba6aea5e89bd226b7509fd8a32a2693a5d2c
        return builder
                .defaultAdvisors(new SecurityPassAdvisor(vectorStore))
                .build();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> f367ba6aea5e89bd226b7509fd8a32a2693a5d2c
