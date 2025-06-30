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
class ResourceRequestControllerTest {

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

    private ResourceRequestDto resourceRequestDto;
    private CreateResourceRequest createRequest;
    private UpdateResourceRequest updateRequest;

    @BeforeEach
    void setUp() {
        resourceRequestDto = new ResourceRequestDto();
        resourceRequestDto.setId(1L);
        resourceRequestDto.setName("Test Request");
        resourceRequestDto.setDescription("Test Description");
        resourceRequestDto.setCategory(ResourceCategory.FOOD);
        resourceRequestDto.setQuantity(10);
        resourceRequestDto.setUnit("kg");
        resourceRequestDto.setStatus(ResourceRequestStatus.PENDING);
        resourceRequestDto.setOrphanageId(1L);

        createRequest = new CreateResourceRequest();
        createRequest.setName("Test Request");
        createRequest.setDescription("Test Description");
        createRequest.setCategory(ResourceCategory.FOOD.toString());
        createRequest.setQuantity(10);
        createRequest.setUnit("kg");
        createRequest.setOrphanageId(1L);

        updateRequest = new UpdateResourceRequest();
        updateRequest.setName("Updated Request");
        updateRequest.setDescription("Updated Description");
        updateRequest.setCategory(ResourceCategory.FOOD.toString());
        updateRequest.setQuantity(20);
        updateRequest.setUnit("kg");
        updateRequest.setStatus(ResourceRequestStatus.PENDING);

        when(securityService.getCurrentUserId()).thenReturn(1L);
        when(securityService.isCurrentUserOrphanageAdmin(anyLong())).thenReturn(true);
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void createResourceRequest_ShouldReturnCreatedRequest() throws Exception {
        when(resourceRequestService.createResourceRequest(any(CreateResourceRequest.class)))
                .thenReturn(resourceRequestDto);

        mockMvc.perform(post("/api/resource-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Request"));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getResourceRequest_ShouldReturnRequest() throws Exception {
        when(resourceRequestService.getResourceRequest(anyLong())).thenReturn(resourceRequestDto);

        mockMvc.perform(get("/api/resource-requests/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resourceRequestDto.getId()))
                .andExpect(jsonPath("$.name").value(resourceRequestDto.getName()));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getAllResourceRequests_ShouldReturnAllRequests() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(resourceRequestDto);
        when(resourceRequestService.getAllResourceRequests()).thenReturn(requests);

        mockMvc.perform(get("/api/resource-requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(resourceRequestDto.getId()))
                .andExpect(jsonPath("$[0].name").value(resourceRequestDto.getName()));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void updateResourceRequest_ShouldUpdateRequest() throws Exception {
        when(resourceRequestService.updateResourceRequest(anyLong(), any(UpdateResourceRequest.class)))
                .thenReturn(resourceRequestDto);

        mockMvc.perform(put("/api/resource-requests/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resourceRequestDto.getId()))
                .andExpect(jsonPath("$.name").value(resourceRequestDto.getName()));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void deleteResourceRequest_ShouldDeleteRequest() throws Exception {
        doNothing().when(resourceRequestService).deleteResourceRequest(anyLong());

        mockMvc.perform(delete("/api/resource-requests/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ORPHANAGE_ADMIN")
    void getResourceRequestsByOrphanage_ShouldReturnOrphanageRequests() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(resourceRequestDto);
        when(resourceRequestService.getResourceRequestsByOrphanage(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/api/resource-requests/orphanage/{orphanageId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(resourceRequestDto.getId()))
                .andExpect(jsonPath("$[0].name").value(resourceRequestDto.getName()));
    }

    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getResourceRequestsByStatus_ShouldReturnRequestsByStatus() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(resourceRequestDto);
        when(resourceRequestService.getResourceRequestsByStatus(any(ResourceRequestStatus.class))).thenReturn(requests);

        mockMvc.perform(get("/api/resource-requests/status/{status}", ResourceRequestStatus.PENDING))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(resourceRequestDto.getId()))
                .andExpect(jsonPath("$[0].name").value(resourceRequestDto.getName()));
    }
} 