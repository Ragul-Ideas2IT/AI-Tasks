package com.connectinghands.service;

import com.connectinghands.dto.AuditLogDto;
import com.connectinghands.entity.AuditLog;
import com.connectinghands.repository.AuditLogRepository;
import com.connectinghands.service.impl.AuditLogServiceImpl;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    private AuditLog auditLog;

    @BeforeEach
    void setUp() {
        auditLog = new AuditLog();
        auditLog.setId(1L);
        auditLog.setUserId(1L);
        auditLog.setAction("CREATE");
        auditLog.setEntityType("User");
        auditLog.setEntityId(1L);
        auditLog.setOldValue(null);
        auditLog.setNewValue("new");
        auditLog.setAdditionalInfo("info");
        auditLog.setIpAddress("127.0.0.1");
        auditLog.setTimestamp(LocalDateTime.now());

        // Set up request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void logAction_Detailed_SavesAuditLog() {
        auditLogService.logAction(1L, "CREATE", "User", 1L, null, "new", "info", "127.0.0.1");
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void logAction_Simple_SavesAuditLog() {
        when(securityService.getCurrentUserId()).thenReturn(1L);
        auditLogService.logAction("CREATE", "desc", 1L);
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void getAuditLogs_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(Collections.singletonList(auditLog));
        when(auditLogRepository.findByFilters(any(), any(), any(), any(), any(), any(), any())).thenReturn(page);
        Page<AuditLogDto> result = auditLogService.getAuditLogs(null, null, null, null, null, null, pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getAuditLog_ValidId_ReturnsDto() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(auditLog));
        AuditLogDto dto = auditLogService.getAuditLog(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getAuditLog_NotFound_ThrowsException() {
        when(auditLogRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> auditLogService.getAuditLog(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Audit log not found");
    }
} 