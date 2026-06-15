package com.enterprise.ai.gateway;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise Bounded Context: State-Aware Diagnostic Simulation Layer
 * Corrects signature bindings to comply fully with Spring AI Message interface contracts.
 */
public class LocalMockChatModel implements ChatModel {

    @Override
    public ChatResponse call(Prompt prompt) {
        // Step 1: Extract and aggregate the prompt instructions using the correct getText() contract handle
        String fullPromptContents = prompt.getInstructions().stream()
                .map(message -> "[" + message.getMessageType() + "]: " + message.getText())
                .collect(Collectors.joining("\n"));

        // Step 2: Construct the diagnostic response matrix reflecting the active heap context state
        String dynamicDiagnosticPayload = """
                ======================================================================
                [GATEWAY DIAGNOSTIC SIMULATION CORE: PERSISTENCE INTEGRATION VALIDATED]
                ======================================================================
                The underlying vector context has been parsed by the model heap layer.
                
                [CAPTURED PROMPT TRACKS]:
                """ + fullPromptContents;

        // Step 3: Package the diagnostic text token into immutable response structures
        AssistantMessage diagnosticAssistantMessage = new AssistantMessage(dynamicDiagnosticPayload);
        Generation generation = new Generation(diagnosticAssistantMessage);

        return new ChatResponse(List.of(generation));
    }
}