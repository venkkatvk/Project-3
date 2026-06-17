// Subsystem Name: Vector Storage & Index Provisioning Layer
// Domain Context: Persistence Infrastructure Group
// File Location: src/main/java/com/enterprise/ai/gateway/RedisDataConfig.java

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
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import redis.clients.jedis.JedisPooled;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Configuration
public class RedisDataConfig {

    /**
     * Local Mock Context: Generates immutable dummy coordinate arrays
     * to satisfy semantic processing without external API handshakes.
     */
    @Bean
    @Primary
    public EmbeddingModel localMockEmbeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                List<Embedding> embeddings = request.getInstructions().stream()
                        .map(text -> new Embedding(new float[]{0.125f, -0.500f, 0.875f}, 0))
                        .toList();
                return new EmbeddingResponse(embeddings);
            }

            @Override
            public float[] embed(Document document) {
                return new float[]{0.125f, -0.500f, 0.875f};
            }
        };
    }

    /**
     * Eagerly provisioned Vector Store bean definition.
     * Removed the @Lazy vulnerability to force index structural validation at startup.
     */
    @Bean
    @Primary
    public VectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        // Explicitly build the storage abstraction layer bound to our clinical domain index
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("healthcare_360_index")
                .initializeSchema(true) // Forces the engine to automatically create the search fields if absent
                .build();
    }

    @Bean
    public JedisPooled jedisPooled(@Value("${spring.data.redis.host:localhost}") String host, 
                                   @Value("${spring.data.redis.port:6379}") int port) {
        return new JedisPooled(host, port);
    }
}