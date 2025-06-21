package com.connectinghands.service;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReportService {
    DonationReportDto generateDonationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone);

    Page<DonationReportDto> generateDonationReports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone,
            Pageable pageable);

    ResourceUtilizationReportDto generateResourceUtilizationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone);

    Page<ResourceUtilizationReportDto> generateResourceUtilizationReports(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone,
            Pageable pageable);

    byte[] exportDonationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String currency,
            String timeZone,
            String format); // PDF, CSV, EXCEL

    byte[] exportResourceUtilizationReport(
            Long orphanageId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String reportType,
            String timeZone,
            String format); // PDF, CSV, EXCEL
} 