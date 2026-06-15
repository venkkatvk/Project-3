package com.enterprise.ai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.context.annotation.Lazy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import redis.clients.jedis.JedisPooled;
import java.util.List;

/**
 * Isolated Bounded Context: Persistence Layer Configuration
 * Configures localized vector storage structures while ensuring absolute offline decoupling.
 */
@Configuration
public class RedisDataConfig {

    /**
     * Atomic Mock Context: Overrides cloud-dependent embedding engines
     * with an instantaneous, type-compliant local vector generation frame.
     */
    @Bean
    @Primary
    public EmbeddingModel localMockEmbeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                // Instantly generates stable, dummy coordinate vectors using a primitive float array layout
                List<Embedding> embeddings = request.getInstructions().stream()
                        .map(text -> new Embedding(new float[]{0.125f, -0.500f, 0.875f}, 0))
                        .toList();
                return new EmbeddingResponse(embeddings);
            }

            @Override
            public float[] embed(Document document) {
                // Fulfills the exact inherited abstract method requirement for primitive document rendering
                return new float[]{0.125f, -0.500f, 0.875f};
            }
        };
    }

    @Bean
    public RedisVectorStore vectorStore(EmbeddingModel embeddingModel) {
        JedisPooled jedisPooled = new JedisPooled("localhost", 6379);
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("healthcare_360_index")
                .build();
    }
    // Add this import
    /**
     * @param jedisPooled
     * @param embeddingModel
     * @return
     */
    @Bean
    @Lazy // This prevents the startup connection error
    public VectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        return new RedisVectorStore(jedisPooled, embeddingModel);
    }
}