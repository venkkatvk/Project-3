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

    private static final int EMBEDDING_DIMENSION = 384;

    static float[] embedText(String text) {
        float[] vector = new float[EMBEDDING_DIMENSION];
        if (text == null || text.isBlank()) {
            return vector;
        }
        for (String word : text.toLowerCase().split("\\W+")) {
            if (word.isEmpty()) {
                continue;
            }
            int index = Math.floorMod(word.hashCode(), vector.length);
            vector[index] += 1.0f;
        }
        float norm = 0.0f;
        for (float value : vector) {
            norm += value * value;
        }
        norm = (float) Math.sqrt(norm);
        if (norm > 0.0f) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= norm;
            }
        }
        return vector;
    }

    @Bean
    @Primary
    public EmbeddingModel localMockEmbeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                List<Embedding> embeddings = request.getInstructions().stream()
                        .map(text -> new Embedding(embedText(text), 0))
                        .toList();
                return new EmbeddingResponse(embeddings);
            }

            @Override
            public float[] embed(Document document) {
                return embedText(document.getText());
            }
        };
    }

    @Bean
    @Primary
    public VectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("healthcare_360_index_v2")
                .initializeSchema(true)
                .build();
    }

    @Bean
    public JedisPooled jedisPooled(@Value("${spring.data.redis.host:localhost}") String host,
                                   @Value("${spring.data.redis.port:6379}") int port) {
        return new JedisPooled(host, port);
    }
}
