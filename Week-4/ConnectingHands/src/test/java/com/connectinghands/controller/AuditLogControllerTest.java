package com.connectinghands.controller;

import com.connectinghands.dto.AuditLogDto;
import com.connectinghands.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.web.servlet.MockMvc;
import com.connectinghands.controller.WithMockCustomUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditLogController.class)
class AuditLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditLogService auditLogService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAuditLogs_ReturnsPage() throws Exception {
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setId(1L);
        auditLog.setUserId(1L);
        auditLog.setAction("test.action");
        auditLog.setEntityType("TestEntity");
        auditLog.setEntityId(1L);

        Page<AuditLogDto> page = new PageImpl<>(List.of(auditLog));
        when(auditLogService.getAuditLogs(any(), any(), any(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/audit-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].userId").value(1))
                .andExpect(jsonPath("$.content[0].action").value("test.action"))
                .andExpect(jsonPath("$.content[0].entityType").value("TestEntity"))
                .andExpect(jsonPath("$.content[0].entityId").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAuditLog_ValidId_ReturnsAuditLog() throws Exception {
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setId(1L);
        auditLog.setUserId(1L);
        auditLog.setAction("test.action");

        when(auditLogService.getAuditLog(1L)).thenReturn(auditLog);

        mockMvc.perform(get("/audit-logs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.action").value("test.action"));
    }

    @Test
    @WithMockCustomUser(id = 1L, roles = {"ADMIN"})
    void getUserAuditLogs_ReturnsPage() throws Exception {
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setId(1L);
        auditLog.setUserId(1L);
        auditLog.setAction("test.action");

        Page<AuditLogDto> page = new PageImpl<>(List.of(auditLog));
        when(auditLogService.getUserAuditLogs(eq(1L), any(), any(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/audit-logs/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].userId").value(1))
                .andExpect(jsonPath("$.content[0].action").value("test.action"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getEntityAuditLogs_ReturnsPage() throws Exception {
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setId(1L);
        auditLog.setEntityType("TestEntity");
        auditLog.setEntityId(1L);

        Page<AuditLogDto> page = new PageImpl<>(List.of(auditLog));
        when(auditLogService.getEntityAuditLogs(eq("TestEntity"), eq(1L), any(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/audit-logs/entity/TestEntity/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].entityType").value("TestEntity"))
                .andExpect(jsonPath("$.content[0].entityId").value(1));
    }

    @Test
    @WithMockUser
    void getAllAuditLogs_Unauthorized_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/audit-logs"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllAuditLogs_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/audit-logs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(id = 2L, roles = {"USER"})
    void getUserAuditLogs_Unauthorized_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/audit-logs/user/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getEntityAuditLogs_Unauthorized_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/audit-logs/entity/TestEntity/1"))
                .andExpect(status().isForbidden());
    }
} 