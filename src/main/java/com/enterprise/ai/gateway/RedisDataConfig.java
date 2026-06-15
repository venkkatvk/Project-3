package com.enterprise.ai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Lazy;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore.Builder;

import redis.clients.jedis.JedisPooled;
import org.springframework.beans.factory.annotation.Value;
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
    @Primary
    @Lazy
    public VectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
          return RedisVectorStore.builder(jedisPooled, embeddingModel)
                    .indexName("healthcare_360_index")
                .build();
    }

    private Builder indexName(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'indexName'");
    }

    @Bean
    public JedisPooled jedisPooled(@Value("${spring.data.redis.host:localhost}") String host, 
                                   @Value("${spring.data.redis.port:6379}") int port) {
        return new JedisPooled(host, port);
    }
}