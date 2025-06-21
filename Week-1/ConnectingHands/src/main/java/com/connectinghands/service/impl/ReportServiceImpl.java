package com.connectinghands.service.impl;

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
import com.connectinghands.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ReportService interface.
 * Handles generation of donation and resource utilization reports.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final DonationRepository donationRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceRequestRepository resourceRequestRepository;
    private final OrphanageRepository orphanageRepository;

    @Override
    @Transactional(readOnly = true)
    public DonationReportDto generateDonationReport(Long orphanageId, LocalDateTime startDate, LocalDateTime endDate,
            String reportType, String currency, String timeZone) {
        Orphanage orphanage = orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new EntityNotFoundException("Orphanage not found"));

        List<Donation> donations = donationRepository.findByOrphanageIdAndCreatedAtBetween(orphanageId, startDate, endDate);

        DonationReportDto report = new DonationReportDto();
        report.setOrphanageId(orphanageId);
        report.setOrphanageName(orphanage.getName());
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setReportType(reportType);
        report.setCurrency(currency);
        report.setTimeZone(timeZone);

        report.setTotalDonations((long) donations.size());
        report.setTotalMonetaryAmount(donations.stream()
                .filter(d -> d.getAmount() != null)
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        report.setTotalResourceDonations(donations.stream()
                .filter(d -> d.getAmount() == null)
                .count());

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DonationReportDto> generateDonationReports(LocalDateTime startDate, LocalDateTime endDate,
            String reportType, String currency, String timeZone, Pageable pageable) {
        Page<Orphanage> orphanages = orphanageRepository.findAll(pageable);

        List<DonationReportDto> reports = orphanages.getContent().stream()
                .map(orphanage -> generateDonationReport(orphanage.getId(), startDate, endDate, reportType, currency, timeZone))
                .collect(Collectors.toList());

        return new PageImpl<>(reports, pageable, orphanages.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceUtilizationReportDto generateResourceUtilizationReport(Long orphanageId) {
        Orphanage orphanage = orphanageRepository.findById(orphanageId)
                .orElseThrow(() -> new EntityNotFoundException("Orphanage not found"));

        List<Resource> resources = resourceRepository.findByOrphanageId(orphanageId);
        Map<String, Long> utilizationMap = new HashMap<>();

        for (Resource resource : resources) {
            utilizationMap.put(resource.getName(), resource.getQuantity().longValue());
        }

        ResourceUtilizationReportDto report = new ResourceUtilizationReportDto();
        report.setOrphanageName(orphanage.getName());
        report.setResourceUtilization(utilizationMap);
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResourceUtilizationReportDto> generateResourceUtilizationReports(LocalDateTime startDate,
            LocalDateTime endDate, String reportType, String timeZone, Pageable pageable) {
        Page<Orphanage> orphanages = orphanageRepository.findAll(pageable);

        List<ResourceUtilizationReportDto> reports = orphanages.getContent().stream()
                .map(orphanage -> generateResourceUtilizationReport(orphanage.getId()))
                .collect(Collectors.toList());

        return new PageImpl<>(reports, pageable, orphanages.getTotalElements());
    }

    @Override
    public byte[] exportDonationReport(Long orphanageId, LocalDateTime startDate, LocalDateTime endDate,
            String reportType, String currency, String timeZone, String format) {
        throw new UnsupportedOperationException("Export functionality not implemented yet");
    }

    @Override
    public byte[] exportResourceUtilizationReport(Long orphanageId, LocalDateTime startDate, LocalDateTime endDate,
            String reportType, String timeZone, String format) {
        throw new UnsupportedOperationException("Export functionality not implemented yet");
    }
} 