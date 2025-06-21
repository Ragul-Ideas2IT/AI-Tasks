package com.connectinghands.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDto {
    private Long id;
    private Long userId;
    private String action;
    private String entityType;
    private Long entityId;
    private String oldValue;
    private String newValue;
    private String additionalInfo;
    private String ipAddress;
    private LocalDateTime timestamp;
} 