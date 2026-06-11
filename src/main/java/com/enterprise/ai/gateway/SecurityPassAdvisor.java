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
import java.util.Map;

public class SecurityPassAdvisor implements CallAdvisor {

    private final VectorStore vectorStore;

    // Injection of the synchronized Redis Vector Store infrastructure component
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
        // 1. Traverse the prompt instruction hierarchy to safely extract raw user message string data
        String userQuery = request.prompt().getInstructions().stream()
                .filter(msg -> msg instanceof UserMessage)
                .map(msg -> msg.getContent())
                .findFirst()
                .orElse("");

        // 2. Instantiate modern SearchRequest via the canonical version 1.1.7 fluent builder
        SearchRequest searchRequest = SearchRequest.builder()
                .query(userQuery)
                .topK(1)
                .similarityThreshold(0.75)
                .build();

        // 3. Scan the containerized background vector keyspace
        List<Document> cachedMatches = this.vectorStore.similaritySearch(searchRequest);

        if (cachedMatches.isEmpty()) {
            // Cache Miss -> Forward request downstream along the advice interception chain
            return chain.nextCall(request);
        } else {
            // Cache Hit -> Short-circuit execution loop and generate a synthetic response wrapper
            Document match = cachedMatches.get(0);
            
            AssistantMessage assistantMessage = new AssistantMessage(match.getContent(), Map.of());
            Generation generation = new Generation(assistantMessage);
            ChatResponse chatResponse = new ChatResponse(List.of(generation));

            return ChatClientResponse.builder()
                    .chatResponse(chatResponse)
                    .build();
        }
    }
}
