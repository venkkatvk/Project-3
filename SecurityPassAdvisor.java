package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise Bounded Context: Interception and Retrieval Augmentation Subsystem
 * Implements the official Spring AI CallAdvisor specification for real-time RAG injections.
 */

@Component
public class SecurityPassAdvisor implements CallAdvisor {

    // Apply the breaker to your retrieval logic
@Override
@CircuitBreaker(name = "vectorStore", fallbackMethod = "fallbackSimilaritySearch")
public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
    String userQuery = chatClientRequest.prompt().getContents();
    
    // Logic delegated to a dedicated method for the breaker to wrap
    List<Document> similarDocuments = performResilientSearch(userQuery);
    
    // ... rest of your existing logic ...
    return callAdvisorChain.nextCall(augmentedRequest);
}

// Fallback logic - This is what happens when the circuit is OPEN
public List<Document> fallbackSimilaritySearch(String query, Throwable t) {
    System.err.println("Circuit Breaker OPEN: Redis unavailable, using empty context. Reason: " + t.getMessage());
    return Collections.emptyList(); // Graceful degradation
}

    // Abstracted to interface layout level to ensure loose coupling and universal bean resolution
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
        return 1;
    }

    /**
     * Intercepts inbound synchronous conversational cycles to execute local vector augmentations.
     */
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        // Step 1: Extract the string query frame from the encapsulated Prompt record
        String userQuery = chatClientRequest.prompt().getContents();

        // Step 2: Execute similarity lookup search requests across localized database indices
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(2)
                        .similarityThreshold(0.7)
                        .build()
        );

        // Step 3: Accumulate text payload segments into a single contextual data block
        String retrievedContext = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        // Step 4: Mutate the prompt structure using the built-in functional instruction augmenter
        Prompt augmentedPrompt = chatClientRequest.prompt().augmentSystemMessage(
                "\n[CRITICAL CLINICAL CONTEXT RESCUED FROM LOCAL REDIS CACHE]:\n" + retrievedContext
        );

        // Step 5: Forge a new immutable request context clone using the fluent mutation builder API
        ChatClientRequest augmentedRequest = chatClientRequest.mutate()
                .prompt(augmentedPrompt)
                .build();

        // Step 6: Advance execution downstream to the next advisor chain unit
        return callAdvisorChain.nextCall(augmentedRequest);

        @CircuitBreaker(name = "vectorStore", fallbackMethod = "fallbackSimilaritySearch")
        final List<Document> executeResilientSearch(String query) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(query).build());
}

// Fallback method returns an empty list if Redis is down
        final List<Document> fallbackSimilaritySearch(String fallbackQuery, Throwable t) {
        System.err.println("Redis unavailable! Executing fallback: " + t.getMessage());
        return List.of(); 
}
    }
}