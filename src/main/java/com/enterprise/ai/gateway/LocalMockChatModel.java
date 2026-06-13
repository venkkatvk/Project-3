package com.enterprise.ai.gateway;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import java.util.List;

/**
 * Isolated Bounded Context: Normalized Local Simulation Subsystem
 * Satisfies the strict functional interface contract of Spring AI v1.1.7.
 */
public class LocalMockChatModel implements ChatModel {

    @Override
    public ChatResponse call(Prompt prompt) {
        // High-performance localized response string payload matching production schema specifications
        String staticPayload = "Gateway Simulation Framework: Connection established. Request processed successfully via Local Mock Subsystem.";
        
        AssistantMessage mockAssistantMessage = new AssistantMessage(staticPayload);
        Generation generation = new Generation(mockAssistantMessage);
        
        return new ChatResponse(List.of(generation));
    }
}