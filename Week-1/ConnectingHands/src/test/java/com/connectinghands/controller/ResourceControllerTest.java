package com.connectinghands.controller;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.service.ResourceService;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for ResourceController.
 * Tests all CRUD operations and security constraints.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    /**
     * Tests creating a resource with admin role.
     * Verifies successful creation and response structure.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void createResource_ShouldReturnCreatedResource() throws Exception {
        // Arrange
        CreateResourceRequest request = new CreateResourceRequest();
        request.setName("Test Resource");
        request.setDescription("Test Description");
        request.setCategory("Test Category");
        request.setQuantity(10);
        request.setUnit("pieces");
        request.setOrphanageId(1L);

        ResourceDto response = new ResourceDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setCategory(request.getCategory());
        response.setQuantity(request.getQuantity());
        response.setUnit(request.getUnit());
        response.setOrphanageId(request.getOrphanageId());

        when(resourceService.createResource(any(CreateResourceRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/resources")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.category").value(request.getCategory()))
                .andExpect(jsonPath("$.quantity").value(request.getQuantity()))
                .andExpect(jsonPath("$.unit").value(request.getUnit()))
                .andExpect(jsonPath("$.orphanageId").value(request.getOrphanageId()));

        verify(resourceService).createResource(any(CreateResourceRequest.class));
    }

    /**
     * Tests retrieving a resource by ID.
     * Verifies successful retrieval and response structure.
     */
    @Test
    @WithMockUser
    void getResource_ShouldReturnResource() throws Exception {
        // Arrange
        ResourceDto resource = new ResourceDto();
        resource.setId(1L);
        resource.setName("Test Resource");
        resource.setDescription("Test Description");
        resource.setCategory("Test Category");
        resource.setQuantity(10);
        resource.setUnit("pieces");
        resource.setOrphanageId(1L);

        when(resourceService.getResource(1L)).thenReturn(resource);

        // Act & Assert
        mockMvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(resource.getName()))
                .andExpect(jsonPath("$.description").value(resource.getDescription()))
                .andExpect(jsonPath("$.category").value(resource.getCategory()))
                .andExpect(jsonPath("$.quantity").value(resource.getQuantity()))
                .andExpect(jsonPath("$.unit").value(resource.getUnit()))
                .andExpect(jsonPath("$.orphanageId").value(resource.getOrphanageId()));

        verify(resourceService).getResource(1L);
    }

    /**
     * Tests retrieving all resources.
     * Verifies successful retrieval and response structure.
     */
    @Test
    @WithMockUser
    void getAllResources_ShouldReturnResourceList() throws Exception {
        // Arrange
        ResourceDto resource1 = new ResourceDto();
        resource1.setId(1L);
        resource1.setName("Resource 1");

        ResourceDto resource2 = new ResourceDto();
        resource2.setId(2L);
        resource2.setName("Resource 2");

        List<ResourceDto> resources = Arrays.asList(resource1, resource2);

        when(resourceService.getAllResources()).thenReturn(resources);

        // Act & Assert
        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Resource 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Resource 2"));

        verify(resourceService).getAllResources();
    }

    /**
     * Tests updating a resource with admin role.
     * Verifies successful update and response structure.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateResource_ShouldReturnUpdatedResource() throws Exception {
        // Arrange
        UpdateResourceRequest request = new UpdateResourceRequest();
        request.setName("Updated Resource");
        request.setDescription("Updated Description");
        request.setCategory("Updated Category");
        request.setQuantity(20);
        request.setUnit("boxes");

        ResourceDto response = new ResourceDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setCategory(request.getCategory());
        response.setQuantity(request.getQuantity());
        response.setUnit(request.getUnit());
        response.setOrphanageId(1L);

        when(resourceService.updateResource(eq(1L), any(UpdateResourceRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/resources/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.category").value(request.getCategory()))
                .andExpect(jsonPath("$.quantity").value(request.getQuantity()))
                .andExpect(jsonPath("$.unit").value(request.getUnit()));

        verify(resourceService).updateResource(eq(1L), any(UpdateResourceRequest.class));
    }

    /**
     * Tests deleting a resource with admin role.
     * Verifies successful deletion.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteResource_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/resources/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(resourceService).deleteResource(1L);
    }

    /**
     * Tests creating a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser
    void createResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        // Arrange
        CreateResourceRequest request = new CreateResourceRequest();
        request.setName("Test Resource");

        // Act & Assert
        mockMvc.perform(post("/resources")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(resourceService, never()).createResource(any());
    }

    /**
     * Tests updating a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser
    void updateResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        // Arrange
        UpdateResourceRequest request = new UpdateResourceRequest();
        request.setName("Updated Resource");

        // Act & Assert
        mockMvc.perform(put("/resources/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(resourceService, never()).updateResource(any(), any());
    }

    /**
     * Tests deleting a resource without admin role.
     * Verifies that the request is forbidden.
     */
    @Test
    @WithMockUser
    void deleteResource_WithoutAdminRole_ShouldReturnForbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/resources/1")
                .with(csrf()))
                .andExpect(status().isForbidden());

        verify(resourceService, never()).deleteResource(any());
    }
} 