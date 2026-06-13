package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Service Configuration Layer Bounded Context
 * Orchestrates bean mapping overrides and binds our perimeter security subsystems.
 */
@Configuration
public class AiGatewayConfig {

    @Bean
    @Primary
    public ChatModel mockChatModel() {
        return new LocalMockChatModel();
    }

    @Bean
    public ChatClient enterpriseChatClient(ChatModel chatModel, VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SecurityPassAdvisor(vectorStore))
                .build();
    }

    // New Atomic Bean Context: Injecting our Stateless Perimeter Guard into the Servlet Chain
    @Bean
    public FilterRegistrationBean<EnterpriseSecurityFilter> securityFilterRegistration() {
        FilterRegistrationBean<EnterpriseSecurityFilter> registrationBean = new FilterRegistrationBean<>();
        
        // Pass our stateless perimeter filter instance into the registry frame
        registrationBean.setFilter(new EnterpriseSecurityFilter());
        
        // Ruthlessly restrict its intercept mapping patterns strictly to our gateway API paths
        registrationBean.addUrlPatterns("/api/v1/*");
        
        // Establish maximum execution priority over lower-level framework filters
        registrationBean.setOrder(1); 
        
        return registrationBean;
    }
}