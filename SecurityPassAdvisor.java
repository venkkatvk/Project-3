package com.enterprise.ai.gateway.advisor;

import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.api.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import java.util.List;

public class SecurityPassAdvisor implements CallAdvisor {
    private final VectorStore vectorStore;

    public SecurityPassAdvisor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        // 1. Extract raw query string via fluent API component lookup
        String userQuery = request.userText();

        // 2. Configure proximity limits using cosine similarity parameters
        SearchRequest searchRequest = SearchRequest.query(userQuery)
                .withTopK(1)
                .withSimilarityThreshold(0.75);

        // 3. Query the Redis Stack search registry
        List<Document> cachedMatches = this.vectorStore.similaritySearch(searchRequest);

        if (cachedMatches.isEmpty()) {
            // 📂 Branch A: Cache Miss -> Delegate downstream to next advisor or live LLM model
            return chain.nextCall(request);
        } else {
            // 🎯 Branch B: Cache Hit -> Short-circuit the request and return the cached text block
            Document match = cachedMatches.get(0);
            ChatResponse cachedChatResponse = new ChatResponse(List.of(new Generation(match.getContent())));
            
            return ChatClientResponse.builder()
                    .chatResponse(cachedChatResponse)
                    .build();
        }
    }
}