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

    // Dependency Injection: Requesting our synchronized vector database toolkit from the core context
    public VectorCachePopulator(VectorStore redisVectorStore) {
        this.vectorStore = redisVectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        // Formulating our pre-cached semantic vector entry payload
        Document preCachedAsset = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("category", "security_clearance_protocol")
        );

        // Seeding the vector database keyspace with our immutable document record
        this.vectorStore.add(List.of(preCachedAsset));
    }
}
