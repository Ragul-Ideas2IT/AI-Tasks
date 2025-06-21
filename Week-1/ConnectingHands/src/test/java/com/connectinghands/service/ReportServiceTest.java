package com.connectinghands.service;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import com.connectinghands.entity.Donation;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.repository.DonationRepository;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRepository;
import com.connectinghands.repository.ResourceRequestRepository;
import com.connectinghands.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceRequestRepository resourceRequestRepository;

    @Mock
    private OrphanageRepository orphanageRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Orphanage orphanage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        orphanage = new Orphanage();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");

        startDate = LocalDateTime.now().minusDays(30);
        endDate = LocalDateTime.now();
    }

    @Test
    void generateDonationReport_ValidData_ReturnsReport() {
        // Arrange
        Donation monetaryDonation = new Donation();
        monetaryDonation.setAmount(new BigDecimal("100.00"));
        monetaryDonation.setOrphanage(orphanage);

        Donation resourceDonation = new Donation();
        resourceDonation.setOrphanage(orphanage);

        when(orphanageRepository.findById(anyLong())).thenReturn(Optional.of(orphanage));
        when(donationRepository.findByOrphanageIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(monetaryDonation, resourceDonation));

        // Act
        DonationReportDto report = reportService.generateDonationReport(
                1L, startDate, endDate, "DETAILED", "USD", "UTC");

        // Assert
        assertThat(report).isNotNull();
        assertThat(report.getOrphanageId()).isEqualTo(1L);
        assertThat(report.getOrphanageName()).isEqualTo("Test Orphanage");
        assertThat(report.getTotalDonations()).isEqualTo(2L);
        assertThat(report.getTotalMonetaryAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(report.getTotalResourceDonations()).isEqualTo(1L);
    }

    @Test
    void generateDonationReports_ValidData_ReturnsPage() {
        // Arrange
        List<Orphanage> orphanages = Collections.singletonList(orphanage);
        Page<Orphanage> orphanagePage = new PageImpl<>(orphanages);

        when(orphanageRepository.findAll(any(Pageable.class))).thenReturn(orphanagePage);
        when(donationRepository.findByOrphanageIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        Page<DonationReportDto> reports = reportService.generateDonationReports(
                startDate, endDate, "SUMMARY", "USD", "UTC", PageRequest.of(0, 10));

        // Assert
        assertThat(reports).isNotNull();
        assertThat(reports.getContent()).hasSize(1);
        assertThat(reports.getContent().get(0).getOrphanageId()).isEqualTo(1L);
        assertThat(reports.getContent().get(0).getOrphanageName()).isEqualTo("Test Orphanage");
    }

    @Test
    void generateResourceUtilizationReport_ValidData_ReturnsReport() {
        // Arrange
        Resource resource1 = new Resource();
        resource1.setName("Food");
        resource1.setQuantity(100);
        resource1.setOrphanage(orphanage);

        Resource resource2 = new Resource();
        resource2.setName("Clothing");
        resource2.setQuantity(50);
        resource2.setOrphanage(orphanage);

        ResourceRequest request = new ResourceRequest();
        request.setName("Food Request");
        request.setQuantity(20);
        request.setOrphanage(orphanage);

        when(orphanageRepository.findById(anyLong())).thenReturn(Optional.of(orphanage));
        when(resourceRepository.findByOrphanageId(anyLong()))
                .thenReturn(Arrays.asList(resource1, resource2));
        when(resourceRequestRepository.findByOrphanageIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(Collections.singletonList(request));

        // Act
        ResourceUtilizationReportDto report = reportService.generateResourceUtilizationReport(
                1L, startDate, endDate, "DETAILED", "UTC");

        // Assert
        assertThat(report).isNotNull();
        assertThat(report.getOrphanageId()).isEqualTo(1L);
        assertThat(report.getOrphanageName()).isEqualTo("Test Orphanage");
        assertThat(report.getResourceUtilization()).containsEntry("Food", 100L);
        assertThat(report.getResourceUtilization()).containsEntry("Clothing", 50L);
        assertThat(report.getResourceRequests()).hasSize(1);
    }

    @Test
    void generateResourceUtilizationReports_ValidData_ReturnsPage() {
        // Arrange
        List<Orphanage> orphanages = Collections.singletonList(orphanage);
        Page<Orphanage> orphanagePage = new PageImpl<>(orphanages);

        when(orphanageRepository.findAll(any(Pageable.class))).thenReturn(orphanagePage);
        when(resourceRepository.findByOrphanageId(anyLong())).thenReturn(Collections.emptyList());
        when(resourceRequestRepository.findByOrphanageIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act
        Page<ResourceUtilizationReportDto> reports = reportService.generateResourceUtilizationReports(
                startDate, endDate, "SUMMARY", "UTC", PageRequest.of(0, 10));

        // Assert
        assertThat(reports).isNotNull();
        assertThat(reports.getContent()).hasSize(1);
        assertThat(reports.getContent().get(0).getOrphanageId()).isEqualTo(1L);
        assertThat(reports.getContent().get(0).getOrphanageName()).isEqualTo("Test Orphanage");
    }

    @Test
    void exportDonationReport_NotImplemented_ThrowsException() {
        // Act & Assert
        assertThatThrownBy(() -> reportService.exportDonationReport(
                1L, startDate, endDate, "DETAILED", "USD", "UTC", "PDF"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Export functionality not implemented yet");
    }

    @Test
    void exportResourceUtilizationReport_NotImplemented_ThrowsException() {
        // Act & Assert
        assertThatThrownBy(() -> reportService.exportResourceUtilizationReport(
                1L, startDate, endDate, "DETAILED", "UTC", "PDF"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Export functionality not implemented yet");
    }
} 