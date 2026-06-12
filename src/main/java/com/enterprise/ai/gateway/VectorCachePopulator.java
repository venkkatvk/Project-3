package com.enterprise.ai.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class VectorCachePopulator implements CommandLineRunner {

    private final VectorStore vectorStore;

    // Constructor Injection: Gathering our vector registry tool from the environment
    public VectorCachePopulator(VectorStore redisVectorStore) {
        this.vectorStore = redisVectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        // Formulating our pre-cached structural document payload
        Document sampleCachedResponse = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("topic", "security_infrastructure")
        );

        // Writing the vectorized data directly into our Redis Stack keyspace index
        this.vectorStore.add(List.of(sampleCachedResponse));
    }
}