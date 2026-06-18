package com.enterprise.ai.gateway;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
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

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        String query = request.prompt().getContents();
        
        // Pure semantic context pull
        List<Document> similarDocuments = this.vectorStore.similaritySearch(
            SearchRequest.builder().query(query).topK(2).build()
        );

        String contextText = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        var augmentedPrompt = request.prompt().augmentSystemMessage(
            "\n[CRITICAL CLINICAL CONTEXT RESCUED FROM LOCAL REDIS CACHE]:\n" + contextText
        );

        return chain.nextCall(request.mutate().prompt(augmentedPrompt).build());
    }
}