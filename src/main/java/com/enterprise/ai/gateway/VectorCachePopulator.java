// Subsystem Name: Context Seed Ingestion Layer
// Domain Context: Persistence Infrastructure Lifecycle Group
// File Location: src/main/java/com/enterprise/ai/gateway/VectorCachePopulator.java

package com.enterprise.ai.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import java.util.List;
import java.util.Map;

@Configuration
public class VectorCachePopulator implements CommandLineRunner {

    private final VectorStore vectorStore;

    // Explicit constructor injection ensuring structural dependencies are met
    public VectorCachePopulator(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("📡 [LIFECYCLE ACTIVATION] Context Seed Ingestion Layer starting up data broadcast...");

        // Fabricating clinical domain mock data blocks for local retrieval
        Document hypertensionRecord = new Document(
            "Patient Profile: Venkkat K. Active treatment protocol for Stage 1 Hypertension. Current daily prescription mapping: Lisinopril 10mg orally once daily. Monitor blood pressure levels twice every week.",
            Map.of("category", "medical_history", "priority", "HIGH")
        );

        Document generalMetricsRecord = new Document(
            "Patient Vital Baselines: Heart rate averages 72 BPM resting. No known allergies to antibiotic agents. Active lifestyle telemetry enabled.",
            Map.of("category", "vital_signs", "priority", "MEDIUM")
        );

        // Bulk-loading documents into the eagerly provisioned vector space
        List<Document> seedBatch = List.of(hypertensionRecord, generalMetricsRecord);
        
        try {
            vectorStore.accept(seedBatch);
            System.out.println("🟩 [LIFECYCLE SUCCESS] 360-Gateway seamlessly ingested clinical seed vectors into storage index!");
        } catch (Exception e) {
            System.err.println("🟥 [LIFECYCLE ERROR] Ingestion Layer failed to write to database node: " + e.getMessage());
        }
    }
}