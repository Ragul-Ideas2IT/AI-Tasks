package com.connectinghands.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ResourceUtilizationReportDto {
    private Long orphanageId;
    private String orphanageName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reportType; // DAILY, WEEKLY, MONTHLY, YEARLY
    private Map<String, Long> resourceUtilization; // Resource name -> Quantity used
    private Map<String, Long> resourceRequests; // Resource name -> Number of requests
    private Map<String, Long> resourceDonations; // Resource name -> Quantity donated
    private List<String> lowStockResources; // Resources below minimum threshold
    private List<String> highDemandResources; // Resources with most requests
    private String timeZone;
} 