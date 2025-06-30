package com.connectinghands.controller;

import com.connectinghands.config.TestConfig;
import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceDto;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import com.connectinghands.service.ResourceService;
import com.connectinghands.service.SecurityService;
import com.connectinghands.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * Test class for ResourceController.
 * Tests all CRUD operations and security constraints.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(ResourceController.class)
@Import(TestConfig.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private AuditLogService auditLogService;

    private ResourceDto resourceDto;
    private CreateResourceRequest createRequest;
    private UpdateResourceDto updateRequest;

    @BeforeEach
    void setUp() {
        resourceDto = new ResourceDto();
        resourceDto.setId(1L);
        resourceDto.setName("Test Resource");
        resourceDto.setDescription("Test Description");
        resourceDto.setCategory(ResourceCategory.FOOD);
        resourceDto.setQuantity(10);
        resourceDto.setUnit("kg");
        resourceDto.setStatus(ResourceStatus.AVAILABLE);

        createRequest = new CreateResourceRequest();
        createRequest.setName("Test Resource");
        createRequest.setDescription("Test Description");
        createRequest.setCategory(ResourceCategory.FOOD.toString());
        createRequest.setQuantity(10);
        createRequest.setUnit("kg");

        updateRequest = new UpdateResourceDto();
        updateRequest.setName("Updated Resource");
        updateRequest.setDescription("Updated Description");
        updateRequest.setCategory(ResourceCategory.FOOD);
        updateRequest.setQuantity(20);
        updateRequest.setUnit("kg");
        updateRequest.setStatus(ResourceStatus.AVAILABLE);

        when(securityService.getCurrentUserId()).thenReturn(1L);
        when(securityService.isCurrentUserOrphanageAdmin(anyLong())).thenReturn(true);
    }

    /**
     * Tests creating a resource with admin role.
     * Verifies successful creation and response structure.
     */
    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void createResource_ShouldReturnCreatedResource() throws Exception {
        when(resourceService.createResource(any(CreateResourceRequest.class))).thenReturn(resourceDto);

        mockMvc.perform(post("/api/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Resource"));
    }

    /**
     * Tests retrieving a resource by ID.
     * Verifies successful retrieval and response structure.
     */
    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getResource_ShouldReturnResource() throws Exception {
        when(resourceService.getResource(anyLong())).thenReturn(resourceDto);

        mockMvc.perform(get("/api/resources/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resourceDto.getId()))
                .andExpect(jsonPath("$.name").value(resourceDto.getName()));
    }

    /**
     * Tests retrieving all resources.
     * Verifies successful retrieval and response structure.
     */
    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void getAllResources_ShouldReturnResourceList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResourceDto> page = new PageImpl<>(Arrays.asList(resourceDto), pageable, 1);
        when(resourceService.getAllResources(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(resourceDto.getId()))
                .andExpect(jsonPath("$.content[0].name").value(resourceDto.getName()));
    }

    /**
     * Tests updating a resource with admin role.
     * Verifies successful update and response structure.
     */
    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void updateResource_ShouldReturnUpdatedResource() throws Exception {
        when(resourceService.updateResource(anyLong(), any(UpdateResourceDto.class))).thenReturn(resourceDto);

        mockMvc.perform(put("/api/resources/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resourceDto.getId()))
                .andExpect(jsonPath("$.name").value(resourceDto.getName()));
    }

    /**
     * Tests deleting a resource with admin role.
     * Verifies successful deletion.
     */
    @Test
    @WithMockUser(roles = "SYSTEM_ADMIN")
    void deleteResource_ShouldReturnOk() throws Exception {
        doNothing().when(resourceService).deleteResource(anyLong());

        mockMvc.perform(delete("/api/resources/{id}", 1L))
                .andExpect(status().isOk());
    }

    /**
     * Tests creating a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser
    void createResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests updating a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser(roles = "USER")
    void updateResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/resources/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests deleting a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser(roles = "USER")
    void deleteResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/resources/{id}", 1L))
                .andExpect(status().isForbidden());
    }
} 