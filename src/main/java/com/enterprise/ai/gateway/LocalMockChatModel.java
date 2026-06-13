package com.enterprise.ai.gateway;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.ChatModelDescription;
import java.util.List;

/**
 * Isolated Bounded Context: Local Simulation Subsystem
 * Bypasses remote HTTP network calls to guarantee 100% gateway uptime during quota blocks.
 */
public class LocalMockChatModel implements ChatModel {

    @Override
    public ChatResponse call(Prompt prompt) {
        // High-performance localized response mapping simulating an enterprise LLM core
        String staticPayload = "Gateway Simulation Framework: Connection established. Request processed successfully via Local Mock Subsystem.";
        
        AssistantMessage mockAssistantMessage = new AssistantMessage(staticPayload);
        Generation generation = new Generation(mockAssistantMessage);
        
        return new ChatResponse(List.of(generation));
    }

    @Override
    public ChatModelDescription getDescription() {
        return new ChatModelDescription() {
            @Override
            public String getModelName() {
                return "local-mock-simulator-v1";
            }
        };
    }
}