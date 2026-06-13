package com.enterprise.ai.gateway;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Isolated Bounded Context: Data Ingestion Subsystem
 * Transforms raw healthcare text into semantically indexed vector fragments.
 */
@Service
public class DataIngestionService {

    private final RedisVectorStore vectorStore;

    public DataIngestionService(RedisVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestHealthcareData(String rawText) {
        // Step 1: Fragment the text into semantically dense chunks
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> documents = splitter.apply(List.of(new Document(rawText)));

        // Step 2: Generate embeddings and persist into Redis
        vectorStore.add(documents);
        
        System.out.println("Ingestion Successful: " + documents.size() + " fragments indexed.");
    }
}