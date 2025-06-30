package com.connectinghands.controller;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.OrphanageStatus;
import com.connectinghands.service.OrphanageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrphanageController.class)
class OrphanageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrphanageService orphanageService;

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void createOrphanage_ValidRequest_ReturnsCreated() throws Exception {
        CreateOrphanageRequest request = new CreateOrphanageRequest();
        request.setName("Test Orphanage");
        request.setDescription("Test Description");
        request.setAddress("123 Test St");
        request.setCity("Test City");
        request.setState("Test State");
        request.setCountry("Test Country");
        request.setPostalCode("12345");
        request.setPhone("1234567890");
        request.setEmail("test@example.com");
        request.setCapacity(100);
        request.setVerificationDocuments("test-docs");

        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setStatus(OrphanageStatus.PENDING);

        when(orphanageService.createOrphanage(any(CreateOrphanageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/orphanages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.PENDING.name()));
    }

    @Test
    @WithMockUser
    void getOrphanage_ValidId_ReturnsOrphanage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setStatus(OrphanageStatus.ACTIVE);

        when(orphanageService.getOrphanage(1L)).thenReturn(orphanage);

        mockMvc.perform(get("/orphanages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.ACTIVE.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllOrphanages_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setStatus(OrphanageStatus.ACTIVE);

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getAllOrphanages(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/orphanages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].status").value(OrphanageStatus.ACTIVE.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrphanagesByStatus_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setStatus(OrphanageStatus.PENDING);

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesByStatus(eq(OrphanageStatus.PENDING), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].status").value(OrphanageStatus.PENDING.name()));
    }

    @Test
    @WithMockUser
    void searchOrphanages_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setCity("Test City");

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.searchOrphanages(eq("test"), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/orphanages/search")
                .param("searchTerm", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].city").value("Test City"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrphanage_ValidRequest_ReturnsUpdated() throws Exception {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        request.setName("Updated Orphanage");
        request.setDescription("Updated Description");

        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        response.setStatus(OrphanageStatus.ACTIVE);

        when(orphanageService.updateOrphanage(eq(1L), any(UpdateOrphanageRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/orphanages/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.ACTIVE.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOrphanage_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/orphanages/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void verifyOrphanage_ValidId_ReturnsVerified() throws Exception {
        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName("Test Orphanage");
        response.setStatus(OrphanageStatus.ACTIVE);

        when(orphanageService.verifyOrphanage(eq(1L), eq("Verified")))
                .thenReturn(response);

        mockMvc.perform(post("/orphanages/1/verify")
                .with(csrf())
                .param("notes", "Verified"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.ACTIVE.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void rejectOrphanage_ValidId_ReturnsRejected() throws Exception {
        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName("Test Orphanage");
        response.setStatus(OrphanageStatus.REJECTED);

        when(orphanageService.rejectOrphanage(eq(1L), eq("Rejected")))
                .thenReturn(response);

        mockMvc.perform(post("/orphanages/1/reject")
                .with(csrf())
                .param("notes", "Rejected"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.REJECTED.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void suspendOrphanage_ValidId_ReturnsSuspended() throws Exception {
        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName("Test Orphanage");
        response.setStatus(OrphanageStatus.SUSPENDED);

        when(orphanageService.suspendOrphanage(eq(1L), eq("Suspended")))
                .thenReturn(response);

        mockMvc.perform(post("/orphanages/1/suspend")
                .with(csrf())
                .param("notes", "Suspended"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.SUSPENDED.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void reactivateOrphanage_ValidId_ReturnsReactivated() throws Exception {
        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName("Test Orphanage");
        response.setStatus(OrphanageStatus.ACTIVE);

        when(orphanageService.reactivateOrphanage(1L)).thenReturn(response);

        mockMvc.perform(post("/orphanages/1/reactivate")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.ACTIVE.name()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void closeOrphanage_ValidId_ReturnsClosed() throws Exception {
        OrphanageDto response = new OrphanageDto();
        response.setId(1L);
        response.setName("Test Orphanage");
        response.setStatus(OrphanageStatus.CLOSED);

        when(orphanageService.closeOrphanage(eq(1L), eq("Closed")))
                .thenReturn(response);

        mockMvc.perform(post("/orphanages/1/close")
                .with(csrf())
                .param("notes", "Closed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Orphanage"))
                .andExpect(jsonPath("$.status").value(OrphanageStatus.CLOSED.name()));
    }

    @Test
    @WithMockUser
    void createOrphanage_Unauthorized_ReturnsForbidden() throws Exception {
        CreateOrphanageRequest request = new CreateOrphanageRequest();
        request.setName("Test Orphanage");

        mockMvc.perform(post("/orphanages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getOrphanage_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/orphanages/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAllOrphanages_Unauthorized_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/orphanages"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ORPHANAGE")
    void createOrphanage_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateOrphanageRequest request = new CreateOrphanageRequest();
        // Missing required fields

        mockMvc.perform(post("/orphanages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrphanage_InvalidId_ReturnsNotFound() throws Exception {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        request.setName("Updated Orphanage");

        when(orphanageService.updateOrphanage(eq(999L), any(UpdateOrphanageRequest.class)))
                .thenThrow(new RuntimeException("Orphanage not found"));

        mockMvc.perform(put("/orphanages/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void verifyOrphanage_InvalidStatus_ReturnsBadRequest() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setStatus(OrphanageStatus.ACTIVE);

        when(orphanageService.verifyOrphanage(eq(1L), eq("Verified")))
                .thenThrow(new IllegalStateException("Cannot verify orphanage in ACTIVE status"));

        mockMvc.perform(post("/orphanages/1/verify")
                .with(csrf())
                .param("notes", "Verified"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrphanagesByCity_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setCity("Test City");

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesByCity(eq("Test City"), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/city/Test City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].city").value("Test City"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrphanagesByState_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setState("Test State");

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesByState(eq("Test State"), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/state/Test State"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].state").value("Test State"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrphanagesByCountry_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setCountry("Test Country");

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesByCountry(eq("Test Country"), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/country/Test Country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].country").value("Test Country"));
    }

    @Test
    @WithMockUser
    void getOrphanagesWithAvailableCapacity_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setCapacity(100);

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesWithAvailableCapacity(any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].capacity").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrphanagesByAdmin_ReturnsPage() throws Exception {
        OrphanageDto orphanage = new OrphanageDto();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setAdminId(1L);

        Page<OrphanageDto> page = new PageImpl<>(List.of(orphanage));
        when(orphanageService.getOrphanagesByAdmin(eq(1L), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/orphanages/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Orphanage"))
                .andExpect(jsonPath("$.content[0].adminId").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrphanage_Unauthorized_ReturnsForbidden() throws Exception {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        request.setName("Updated Orphanage");

        when(orphanageService.updateOrphanage(eq(1L), any(UpdateOrphanageRequest.class)))
                .thenThrow(new RuntimeException("Not authorized to update this orphanage"));

        mockMvc.perform(put("/orphanages/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOrphanage_InvalidId_ReturnsNotFound() throws Exception {
        doThrow(new RuntimeException("Orphanage not found"))
                .when(orphanageService).deleteOrphanage(999L);

        mockMvc.perform(delete("/orphanages/999")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
} 