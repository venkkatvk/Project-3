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

<<<<<<< HEAD
    // Constructor Injection: Gathering our vector registry tool from the environment
=======
    // Dependency Injection: Requesting our synchronized vector database toolkit from the core context
>>>>>>> f367ba6aea5e89bd226b7509fd8a32a2693a5d2c
    public VectorCachePopulator(VectorStore redisVectorStore) {
        this.vectorStore = redisVectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
<<<<<<< HEAD
        // Formulating our pre-cached structural document payload
        Document sampleCachedResponse = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("topic", "security_infrastructure")
        );

        // Writing the vectorized data directly into our Redis Stack keyspace index
        this.vectorStore.add(List.of(sampleCachedResponse));
    }
}
=======
        // Formulating our pre-cached semantic vector entry payload
        Document preCachedAsset = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("category", "security_clearance_protocol")
        );

        // Seeding the vector database keyspace with our immutable document record
        this.vectorStore.add(List.of(preCachedAsset));
    }
}
>>>>>>> f367ba6aea5e89bd226b7509fd8a32a2693a5d2c
