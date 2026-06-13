package com.enterprise.ai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import redis.clients.jedis.JedisPooled;

/**
 * Isolated Bounded Context: Persistence Layer Integration
 * Binds the gateway to a persistent Redis vector storage engine.
 */
@Configuration
public class RedisDataConfig {

    @Bean
    public RedisVectorStore vectorStore(EmbeddingModel embeddingModel) {
        // Initializes the pooled connection to the Redis infrastructure
        JedisPooled jedisPooled = new JedisPooled("localhost", 6379);
        
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("healthcare_360_index")
                .prefix("patient_data:")
                .build();
    }
}