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

    // Dependency Injection: Binding our synchronized Redis infrastructure store layer
    public VectorCachePopulator(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        // Instantiate the canonical pre-cached secure semantic payload document
        Document preCachedAsset = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("category", "security_clearance_protocol")
        );

        // Seed the high-dimensional vector space cache engine natively
        this.vectorStore.add(List.of(preCachedAsset));
    }
}