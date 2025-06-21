package com.connectinghands.service.impl;

import com.connectinghands.dto.AuditLogDto;
import com.connectinghands.entity.AuditLog;
import com.connectinghands.repository.AuditLogRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Implementation of the AuditLogService interface.
 * Handles audit logging for all operations in the system.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final SecurityService securityService;

    @Override
    @Transactional
    public void logAction(Long userId, String action, String entityType, Long entityId,
                         String oldValue, String newValue, String additionalInfo, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setAdditionalInfo(additionalInfo);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logAction(String action, String description, Long entityId) {
        Long userId = securityService.getCurrentUserId();
        String ipAddress = getCurrentUserIpAddress();
        
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType("SYSTEM");
        log.setEntityId(entityId);
        log.setAdditionalInfo(description);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogs(Long userId, String action, String entityType, Long entityId,
                                         LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByFilters(userId, action, entityType, entityId, startDate, endDate, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogDto getAuditLog(Long id) {
        return auditLogRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Audit log not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getUserAuditLogs(Long userId, String action, String entityType, Long entityId,
                                            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByUserIdAndFilters(userId, action, entityType, entityId, startDate, endDate, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getEntityAuditLogs(String entityType, Long entityId, Long userId, String action,
                                              LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityIdAndFilters(entityType, entityId, userId, action, startDate, endDate, pageable)
                .map(this::mapToDto);
    }

    private AuditLogDto mapToDto(AuditLog log) {
        AuditLogDto dto = new AuditLogDto();
        dto.setId(log.getId());
        dto.setUserId(log.getUserId());
        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());
        dto.setOldValue(log.getOldValue());
        dto.setNewValue(log.getNewValue());
        dto.setAdditionalInfo(log.getAdditionalInfo());
        dto.setIpAddress(log.getIpAddress());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }

    private String getCurrentUserIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRemoteAddr();
    }
} 