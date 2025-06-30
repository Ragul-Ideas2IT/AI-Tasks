package com.connectinghands.controller;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import com.connectinghands.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void getDonationReport_ValidRequest_ReturnsReport() throws Exception {
        // Arrange
        DonationReportDto report = new DonationReportDto();
        report.setOrphanageId(1L);
        report.setOrphanageName("Test Orphanage");
        report.setTotalDonations(10L);
        report.setTotalMonetaryAmount(new BigDecimal("1000.00"));
        report.setTotalResourceDonations(5L);

        when(reportService.generateDonationReport(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString(), anyString()))
                .thenReturn(report);

        // Act & Assert
        mockMvc.perform(get("/api/reports/donations/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "DETAILED")
                .param("currency", "USD")
                .param("timeZone", "UTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orphanageId").value(1))
                .andExpect(jsonPath("$.orphanageName").value("Test Orphanage"))
                .andExpect(jsonPath("$.totalDonations").value(10))
                .andExpect(jsonPath("$.totalMonetaryAmount").value(1000.00))
                .andExpect(jsonPath("$.totalResourceDonations").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDonationReports_ValidRequest_ReturnsPage() throws Exception {
        // Arrange
        DonationReportDto report = new DonationReportDto();
        report.setOrphanageId(1L);
        report.setOrphanageName("Test Orphanage");
        report.setTotalDonations(10L);
        report.setTotalMonetaryAmount(new BigDecimal("1000.00"));
        report.setTotalResourceDonations(5L);

        Page<DonationReportDto> page = new PageImpl<>(Collections.singletonList(report));

        when(reportService.generateDonationReports(
                any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/reports/donations")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "SUMMARY")
                .param("currency", "USD")
                .param("timeZone", "UTC")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orphanageId").value(1))
                .andExpect(jsonPath("$.content[0].orphanageName").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].totalDonations").value(10))
                .andExpect(jsonPath("$.content[0].totalMonetaryAmount").value(1000.00))
                .andExpect(jsonPath("$.content[0].totalResourceDonations").value(5));
    }

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void getResourceUtilizationReport_ValidRequest_ReturnsReport() throws Exception {
        // Arrange
        ResourceUtilizationReportDto report = new ResourceUtilizationReportDto();
        report.setOrphanageId(1L);
        report.setOrphanageName("Test Orphanage");
        
        Map<String, Long> utilization = new HashMap<>();
        utilization.put("Food", 100L);
        utilization.put("Clothing", 50L);
        report.setResourceUtilization(utilization);

        when(reportService.generateResourceUtilizationReport(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString()))
                .thenReturn(report);

        // Act & Assert
        mockMvc.perform(get("/api/reports/resources/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "DETAILED")
                .param("timeZone", "UTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orphanageId").value(1))
                .andExpect(jsonPath("$.orphanageName").value("Test Orphanage"))
                .andExpect(jsonPath("$.resourceUtilization.Food").value(100))
                .andExpect(jsonPath("$.resourceUtilization.Clothing").value(50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllResourceUtilizationReports_ValidRequest_ReturnsPage() throws Exception {
        // Arrange
        ResourceUtilizationReportDto report = new ResourceUtilizationReportDto();
        report.setOrphanageId(1L);
        report.setOrphanageName("Test Orphanage");
        
        Map<String, Long> utilization = new HashMap<>();
        utilization.put("Food", 100L);
        utilization.put("Clothing", 50L);
        report.setResourceUtilization(utilization);

        Page<ResourceUtilizationReportDto> page = new PageImpl<>(Collections.singletonList(report));

        when(reportService.generateResourceUtilizationReports(
                any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/reports/resources")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "SUMMARY")
                .param("timeZone", "UTC")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orphanageId").value(1))
                .andExpect(jsonPath("$.content[0].orphanageName").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].resourceUtilization.Food").value(100))
                .andExpect(jsonPath("$.content[0].resourceUtilization.Clothing").value(50));
    }

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void exportDonationReport_ValidRequest_ReturnsFile() throws Exception {
        // Arrange
        byte[] reportData = "PDF content".getBytes();

        when(reportService.exportDonationReport(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(reportData);

        // Act & Assert
        mockMvc.perform(get("/api/reports/donations/1/export")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "DETAILED")
                .param("currency", "USD")
                .param("timeZone", "UTC")
                .param("format", "PDF"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void exportResourceUtilizationReport_ValidRequest_ReturnsFile() throws Exception {
        // Arrange
        byte[] reportData = "PDF content".getBytes();

        when(reportService.exportResourceUtilizationReport(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class),
                anyString(), anyString(), anyString()))
                .thenReturn(reportData);

        // Act & Assert
        mockMvc.perform(get("/api/reports/resources/1/export")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59")
                .param("reportType", "DETAILED")
                .param("timeZone", "UTC")
                .param("format", "PDF"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    @WithMockUser(roles = "DONOR")
    void getDonationReport_UnauthorizedRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/reports/donations/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DONOR")
    void getResourceUtilizationReport_UnauthorizedRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/reports/resources/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getDonationReport_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reports/donations/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getResourceUtilizationReport_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reports/resources/1")
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isUnauthorized());
    }
} 