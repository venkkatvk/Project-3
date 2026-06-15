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
import io.micrometer.observation.annotation.Observed; // Import this!
import java.util.List;
import java.util.stream.Collectors;

@Component
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
        return 1;
    }

    // THIS IS THE ONLY ADVISECALL METHOD
    @Override
    @Observed(name = "rag.retrieval.latency", contextualName = "vector-store-search")
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        String userQuery = chatClientRequest.prompt().getContents();

        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(2)
                        .similarityThreshold(0.7)
                        .build()
        );

        String retrievedContext = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        Prompt augmentedPrompt = chatClientRequest.prompt().augmentSystemMessage(
                "\n[CRITICAL CLINICAL CONTEXT RESCUED FROM LOCAL REDIS CACHE]:\n" + retrievedContext
        );

        ChatClientRequest augmentedRequest = chatClientRequest.mutate()
                .prompt(augmentedPrompt)
                .build();

        return callAdvisorChain.nextCall(augmentedRequest);
    }
}