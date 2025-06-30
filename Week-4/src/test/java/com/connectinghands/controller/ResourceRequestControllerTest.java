package com.connectinghands.controller;

import com.connectinghands.config.TestConfig;
import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.service.ResourceRequestService;
import com.connectinghands.service.SecurityService;
import com.connectinghands.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ResourceRequestController.
 * Tests all endpoints with proper security constraints and validation.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(ResourceRequestController.class)
@Import(TestConfig.class)
public class ResourceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceRequestService resourceRequestService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private AuditLogService auditLogService;

    private ResourceRequestDto testResourceRequest;

    @BeforeEach
    void setUp() {
        testResourceRequest = new ResourceRequestDto();
        testResourceRequest.setId(1L);
        testResourceRequest.setTitle("Test Resource Request");
        testResourceRequest.setDescription("Test Description");
        testResourceRequest.setCategory(ResourceCategory.BOOKS);
        testResourceRequest.setStatus(ResourceRequestStatus.PENDING);
        testResourceRequest.setQuantity(5);
        testResourceRequest.setOrphanageId(1L);
        testResourceRequest.setRequesterId(1L);

        when(securityService.getCurrentUserId()).thenReturn(1L);
        when(securityService.isCurrentUserOrphanageAdmin(anyLong())).thenReturn(true);
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void createResourceRequest_ValidRequest_ReturnsCreated() throws Exception {
        CreateResourceRequest request = new CreateResourceRequest();
        request.setTitle("New Resource Request");
        request.setDescription("New Description");
        request.setCategory(ResourceCategory.BOOKS);
        request.setQuantity(5);
        request.setOrphanageId(1L);

        when(resourceRequestService.createResourceRequest(any(CreateResourceRequest.class)))
            .thenReturn(testResourceRequest);

        mockMvc.perform(post("/api/resource-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Resource Request"));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getResourceRequest_ValidId_ReturnsResourceRequest() throws Exception {
        when(resourceRequestService.getResourceRequest(1L)).thenReturn(testResourceRequest);

        mockMvc.perform(get("/api/resource-requests/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Resource Request"));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void updateResourceRequest_ValidRequest_ReturnsUpdated() throws Exception {
        UpdateResourceRequest request = new UpdateResourceRequest();
        request.setTitle("Updated Resource Request");
        request.setDescription("Updated Description");
        request.setStatus(ResourceRequestStatus.APPROVED);

        when(resourceRequestService.updateResourceRequest(anyLong(), any(UpdateResourceRequest.class)))
            .thenReturn(testResourceRequest);

        mockMvc.perform(put("/api/resource-requests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void deleteResourceRequest_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(resourceRequestService).deleteResourceRequest(1L);

        mockMvc.perform(delete("/api/resource-requests/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getAllResourceRequests_ReturnsResourceRequestList() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(testResourceRequest);
        when(resourceRequestService.getAllResourceRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/resource-requests"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Test Resource Request"));
    }
} 