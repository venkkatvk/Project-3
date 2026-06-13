package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import java.util.List;
import java.util.Collections;

public class SecurityPassAdvisor implements CallAdvisor {

    private final VectorStore vectorStore;

    public SecurityPassAdvisor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public String getName() {
        return "SecurityPassAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        String userQuery = request.prompt().getInstructions().stream()
                .filter(msg -> msg instanceof UserMessage)
                .map(msg -> msg.getText())
                .findFirst()
                .orElse("");

        SearchRequest searchRequest = SearchRequest.builder()
                .query(userQuery)
                .topK(1)
                .similarityThreshold(0.75)
                .build();

        List<Document> cachedMatches;
        try {
            // Synchronous vector conversion execution point
            cachedMatches = this.vectorStore.similaritySearch(searchRequest);
        } catch (Exception e) {
            // Resilience Fallback: Catch quota/network faults, log them, and enforce a safe Cache Miss fallback path
            System.out.println("[Resilience Engine] Downstream vector store unavailable due to cloud quota limits. Bypassing search.");
            cachedMatches = Collections.emptyList();
        }

        if (cachedMatches.isEmpty()) {
            // Forward payload seamlessly down the interceptor chain
            return chain.nextCall(request);
        } else {
            Document match = cachedMatches.get(0);
            AssistantMessage assistantMessage = new AssistantMessage(match.getText());
            Generation generation = new Generation(assistantMessage);
            ChatResponse chatResponse = new ChatResponse(List.of(generation));

            return ChatClientResponse.builder()
                    .chatResponse(chatResponse)
                    .build();
        }
    }
}