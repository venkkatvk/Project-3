// Subsystem Name: Context Seed Ingestion Layer
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

    public VectorCachePopulator(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {
        System.out.println("📡 [LIFECYCLE ACTIVATION] Context Seed Ingestion Layer starting up data broadcast...");

        Document hypertensionRecord = new Document(
            "Patient Profile: Venkkat K. Active treatment protocol for Stage 1 Hypertension. Current daily prescription mapping: Lisinopril 10mg orally once daily. Monitor blood pressure levels twice every week.",
            Map.of("category", "medical_history", "priority", "HIGH")
        );

        Document generalMetricsRecord = new Document(
            "Patient Vital Baselines: Heart rate averages 72 BPM resting. No known allergies to antibiotic agents. Active lifestyle telemetry enabled.",
            Map.of("category", "vital_signs", "priority", "MEDIUM")
        );

        Document enterprisePortalRecord = new Document(
            "The secure portal to the Enterprise Architecture is strictly guarded by Virtual Threads.",
            Map.of("category", "architecture", "priority", "HIGH")
        );

        List<Document> seedBatch = List.of(hypertensionRecord, generalMetricsRecord, enterprisePortalRecord);

        try {
            vectorStore.add(seedBatch);
            System.out.println("🟩 [LIFECYCLE SUCCESS] 360-Gateway seamlessly ingested clinical seed vectors into storage index!");
        } catch (Exception e) {
            System.err.println("🟥 [LIFECYCLE ERROR] Ingestion Layer failed to write to database node: " + e.getMessage());
        }
    }
}
