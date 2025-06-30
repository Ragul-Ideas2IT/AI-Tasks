package com.connectinghands.controller;

import com.connectinghands.dto.DonationReportDto;
import com.connectinghands.dto.ResourceUtilizationReportDto;
import com.connectinghands.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "API for generating and exporting reports")
@SecurityRequirement(name = "JWT")
public class ReportController {
    private final ReportService reportService;

    @Operation(
        summary = "Get donation report for an orphanage",
        description = "Retrieves a detailed donation report for a specific orphanage within a date range"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report generated successfully",
            content = @Content(schema = @Schema(implementation = DonationReportDto.class))),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Orphanage not found")
    })
    @GetMapping("/donations/{orphanageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<DonationReportDto> getDonationReport(
            @Parameter(description = "ID of the orphanage") @PathVariable Long orphanageId,
            @Parameter(description = "Start date for the report period") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate") 
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @Parameter(description = "Currency for monetary amounts") 
            @RequestParam(defaultValue = "USD") String currency,
            @Parameter(description = "Time zone for date/time values") 
            @RequestParam(defaultValue = "UTC") String timeZone) {
        
        return ResponseEntity.ok(reportService.generateDonationReport(
                orphanageId, startDate, endDate, reportType, currency, timeZone));
    }

    @Operation(
        summary = "Get donation reports for all orphanages",
        description = "Retrieves donation reports for all orphanages within a date range"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports generated successfully",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/donations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DonationReportDto>> getAllDonationReports(
            @Parameter(description = "Start date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate")
            @RequestParam(defaultValue = "SUMMARY") String reportType,
            @Parameter(description = "Currency for monetary amounts")
            @RequestParam(defaultValue = "USD") String currency,
            @Parameter(description = "Time zone for date/time values")
            @RequestParam(defaultValue = "UTC") String timeZone,
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        
        return ResponseEntity.ok(reportService.generateDonationReports(
                startDate, endDate, reportType, currency, timeZone, pageable));
    }

    @Operation(
        summary = "Get resource utilization report for an orphanage",
        description = "Retrieves a detailed resource utilization report for a specific orphanage within a date range"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report generated successfully",
            content = @Content(schema = @Schema(implementation = ResourceUtilizationReportDto.class))),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Orphanage not found")
    })
    @GetMapping("/resources/{orphanageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<ResourceUtilizationReportDto> getResourceUtilizationReport(
            @Parameter(description = "ID of the orphanage") @PathVariable Long orphanageId,
            @Parameter(description = "Start date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate")
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @Parameter(description = "Time zone for date/time values")
            @RequestParam(defaultValue = "UTC") String timeZone) {
        
        return ResponseEntity.ok(reportService.generateResourceUtilizationReport(
                orphanageId, startDate, endDate, reportType, timeZone));
    }

    @Operation(
        summary = "Get resource utilization reports for all orphanages",
        description = "Retrieves resource utilization reports for all orphanages within a date range"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports generated successfully",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/resources")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResourceUtilizationReportDto>> getAllResourceUtilizationReports(
            @Parameter(description = "Start date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate")
            @RequestParam(defaultValue = "SUMMARY") String reportType,
            @Parameter(description = "Time zone for date/time values")
            @RequestParam(defaultValue = "UTC") String timeZone,
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {
        
        return ResponseEntity.ok(reportService.generateResourceUtilizationReports(
                startDate, endDate, reportType, timeZone, pageable));
    }

    @Operation(
        summary = "Export donation report",
        description = "Exports a donation report for a specific orphanage in the specified format"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report exported successfully",
            content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Orphanage not found")
    })
    @GetMapping("/donations/{orphanageId}/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<byte[]> exportDonationReport(
            @Parameter(description = "ID of the orphanage") @PathVariable Long orphanageId,
            @Parameter(description = "Start date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate")
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @Parameter(description = "Currency for monetary amounts")
            @RequestParam(defaultValue = "USD") String currency,
            @Parameter(description = "Time zone for date/time values")
            @RequestParam(defaultValue = "UTC") String timeZone,
            @Parameter(description = "Export format (PDF, CSV, EXCEL)")
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] reportData = reportService.exportDonationReport(
                orphanageId, startDate, endDate, reportType, currency, timeZone, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(format));
        headers.setContentDispositionFormData("attachment", 
                String.format("donation-report-%d.%s", orphanageId, format.toLowerCase()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }

    @Operation(
        summary = "Export resource utilization report",
        description = "Exports a resource utilization report for a specific orphanage in the specified format"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report exported successfully",
            content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Orphanage not found")
    })
    @GetMapping("/resources/{orphanageId}/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORPHANAGE')")
    public ResponseEntity<byte[]> exportResourceUtilizationReport(
            @Parameter(description = "ID of the orphanage") @PathVariable Long orphanageId,
            @Parameter(description = "Start date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the report period")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Type of report to generate")
            @RequestParam(defaultValue = "DETAILED") String reportType,
            @Parameter(description = "Time zone for date/time values")
            @RequestParam(defaultValue = "UTC") String timeZone,
            @Parameter(description = "Export format (PDF, CSV, EXCEL)")
            @RequestParam(defaultValue = "PDF") String format) {
        
        byte[] reportData = reportService.exportResourceUtilizationReport(
                orphanageId, startDate, endDate, reportType, timeZone, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getMediaType(format));
        headers.setContentDispositionFormData("attachment", 
                String.format("resource-utilization-report-%d.%s", orphanageId, format.toLowerCase()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }

    private MediaType getMediaType(String format) {
        return switch (format.toUpperCase()) {
            case "PDF" -> MediaType.APPLICATION_PDF;
            case "CSV" -> MediaType.parseMediaType("text/csv");
            case "EXCEL" -> MediaType.parseMediaType("application/vnd.ms-excel");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
