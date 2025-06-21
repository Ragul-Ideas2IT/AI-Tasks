package com.connectinghands.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonationReportDto {
    private Long orphanageId;
    private String orphanageName;
    private Long totalDonations;
    private BigDecimal totalMonetaryAmount;
    private Long totalResourceDonations;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reportType; // DAILY, WEEKLY, MONTHLY, YEARLY
    private String currency;
    private String timeZone;
} 