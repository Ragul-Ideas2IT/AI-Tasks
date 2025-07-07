package com.collabdocs.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        // Stubbed data; in real system, aggregate from other services
        return Map.of(
            "totalUsers", 42,
            "totalDocuments", 128,
            "activeCollaborations", 7
        );
    }
} 