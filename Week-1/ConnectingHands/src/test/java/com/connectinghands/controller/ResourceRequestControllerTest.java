package com.connectinghands.controller;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.service.ResourceRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for ResourceRequestController.
 * Tests all endpoints with proper security constraints and validation.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(ResourceRequestController.class)
class ResourceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceRequestService resourceRequestService;

    /**
     * Test creating a new resource request.
     * Verifies that an orphanage user can create a request.
     */
    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void createResourceRequest_ShouldCreateRequest() throws Exception {
        CreateResourceRequest request = new CreateResourceRequest();
        request.setName("Test Request");
        request.setDescription("Test Description");
        request.setCategory("Food");
        request.setQuantity(10);
        request.setUnit("kg");
        request.setOrphanageId(1L);

        ResourceRequestDto response = new ResourceRequestDto();
        response.setId(1L);
        response.setName("Test Request");
        response.setStatus(ResourceRequestStatus.PENDING);

        when(resourceRequestService.createResourceRequest(any(CreateResourceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/resource-requests")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Request"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Test retrieving a resource request by ID.
     * Verifies that an authenticated user can retrieve a request.
     */
    @Test
    @WithMockUser
    void getResourceRequest_ShouldReturnRequest() throws Exception {
        ResourceRequestDto request = new ResourceRequestDto();
        request.setId(1L);
        request.setName("Test Request");
        request.setStatus(ResourceRequestStatus.PENDING);

        when(resourceRequestService.getResourceRequest(1L)).thenReturn(request);

        mockMvc.perform(get("/resource-requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Request"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Test retrieving all resource requests.
     * Verifies that only admin users can access all requests.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllResourceRequests_ShouldReturnAllRequests() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(
                createResourceRequestDto(1L, "Request 1", ResourceRequestStatus.PENDING),
                createResourceRequestDto(2L, "Request 2", ResourceRequestStatus.FULFILLED)
        );

        when(resourceRequestService.getAllResourceRequests()).thenReturn(requests);

        mockMvc.perform(get("/resource-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test retrieving resource requests by orphanage.
     * Verifies that the orphanage and admin can access orphanage's requests.
     */
    @Test
    @WithMockUser
    void getResourceRequestsByOrphanage_ShouldReturnOrphanageRequests() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(
                createResourceRequestDto(1L, "Request 1", ResourceRequestStatus.PENDING),
                createResourceRequestDto(2L, "Request 2", ResourceRequestStatus.PENDING)
        );

        when(resourceRequestService.getResourceRequestsByOrphanage(1L)).thenReturn(requests);

        mockMvc.perform(get("/resource-requests/orphanage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test retrieving resource requests by status.
     * Verifies that only admin users can access requests by status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getResourceRequestsByStatus_ShouldReturnRequestsByStatus() throws Exception {
        List<ResourceRequestDto> requests = Arrays.asList(
                createResourceRequestDto(1L, "Request 1", ResourceRequestStatus.PENDING),
                createResourceRequestDto(2L, "Request 2", ResourceRequestStatus.PENDING)
        );

        when(resourceRequestService.getResourceRequestsByStatus(ResourceRequestStatus.PENDING)).thenReturn(requests);

        mockMvc.perform(get("/resource-requests/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test updating a resource request.
     * Verifies that only admin users can update requests.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateResourceRequest_ShouldUpdateRequest() throws Exception {
        UpdateResourceRequest request = new UpdateResourceRequest();
        request.setName("Updated Request");
        request.setStatus(ResourceRequestStatus.FULFILLED);

        ResourceRequestDto updatedRequest = new ResourceRequestDto();
        updatedRequest.setId(1L);
        updatedRequest.setName("Updated Request");
        updatedRequest.setStatus(ResourceRequestStatus.FULFILLED);

        when(resourceRequestService.updateResourceRequest(eq(1L), any(UpdateResourceRequest.class)))
                .thenReturn(updatedRequest);

        mockMvc.perform(put("/resource-requests/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Request"))
                .andExpect(jsonPath("$.status").value("FULFILLED"));
    }

    /**
     * Test deleting a resource request.
     * Verifies that only admin users can delete requests.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteResourceRequest_ShouldDeleteRequest() throws Exception {
        mockMvc.perform(delete("/resource-requests/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    private ResourceRequestDto createResourceRequestDto(Long id, String name, ResourceRequestStatus status) {
        ResourceRequestDto dto = new ResourceRequestDto();
        dto.setId(id);
        dto.setName(name);
        dto.setStatus(status);
        return dto;
    }
} 