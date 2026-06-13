package com.enterprise.ai.gateway;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminIngestionController {

    private final DataIngestionService ingestionService;

    public AdminIngestionController(DataIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public String ingest(@RequestBody String data) {
        ingestionService.ingestHealthcareData(data);
        return "Data successfully ingested into Redis Healthcare Index.";
    }
}