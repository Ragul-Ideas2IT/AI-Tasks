package com.connectinghands.service;

import com.connectinghands.dto.AuditLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Service interface for managing audit logs.
 * 
 * @author Ragul Venkatesan
 */
public interface AuditLogService {
    /**
     * Log an action with detailed information.
     *
     * @param userId the ID of the user performing the action
     * @param action the action being performed
     * @param entityType the type of entity being acted upon
     * @param entityId the ID of the entity being acted upon
     * @param oldValue the previous value (if applicable)
     * @param newValue the new value (if applicable)
     * @param additionalInfo additional information about the action
     * @param ipAddress the IP address of the user
     */
    void logAction(Long userId, String action, String entityType, Long entityId,
                  String oldValue, String newValue, String additionalInfo, String ipAddress);

    /**
     * Log a simple action.
     *
     * @param action the action being performed
     * @param description a description of the action
     * @param entityId the ID of the entity being acted upon
     */
    void logAction(String action, String description, Long entityId);

    Page<AuditLogDto> getAuditLogs(Long userId, String action, String entityType, Long entityId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    AuditLogDto getAuditLog(Long id);
    
    Page<AuditLogDto> getUserAuditLogs(Long userId, String action, String entityType, Long entityId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<AuditLogDto> getEntityAuditLogs(String entityType, Long entityId, Long userId, String action, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
} 