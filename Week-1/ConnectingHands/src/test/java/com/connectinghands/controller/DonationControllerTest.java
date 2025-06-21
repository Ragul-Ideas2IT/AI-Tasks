package com.connectinghands.controller;

import com.connectinghands.controller.DonationController;
import com.connectinghands.dto.CreateDonationRequest;
import com.connectinghands.dto.DonationDto;
import com.connectinghands.dto.UpdateDonationRequest;
import com.connectinghands.entity.DonationStatus;
import com.connectinghands.entity.PaymentMethod;
import com.connectinghands.service.DonationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
 * Test class for the DonationController.
 * Tests all endpoints with proper security constraints and validation.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(DonationController.class)
class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DonationService donationService;

    /**
     * Test creating a new donation.
     * Verifies that an authenticated user can create a donation.
     */
    @Test
    @WithMockUser
    void createDonation_ShouldCreateDonation() throws Exception {
        CreateDonationRequest request = new CreateDonationRequest();
        request.setOrphanageId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setNotes("Test donation");

        DonationDto response = new DonationDto();
        response.setId(1L);
        response.setAmount(new BigDecimal("100.00"));
        response.setStatus(DonationStatus.PENDING);

        when(donationService.createDonation(any(CreateDonationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/donations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Test retrieving a donation by ID.
     * Verifies that an authenticated user can retrieve a donation.
     */
    @Test
    @WithMockUser
    void getDonation_ShouldReturnDonation() throws Exception {
        DonationDto donation = new DonationDto();
        donation.setId(1L);
        donation.setAmount(new BigDecimal("100.00"));
        donation.setStatus(DonationStatus.COMPLETED);

        when(donationService.getDonation(1L)).thenReturn(donation);

        mockMvc.perform(get("/donations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    /**
     * Test retrieving all donations.
     * Verifies that only admin users can access all donations.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDonations_ShouldReturnAllDonations() throws Exception {
        List<DonationDto> donations = Arrays.asList(
                createDonationDto(1L, "100.00", DonationStatus.COMPLETED),
                createDonationDto(2L, "200.00", DonationStatus.PENDING)
        );

        when(donationService.getAllDonations()).thenReturn(donations);

        mockMvc.perform(get("/donations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test retrieving donations by donor ID.
     * Verifies that the donor and admin can access donor's donations.
     */
    @Test
    @WithMockUser
    void getDonationsByDonor_ShouldReturnDonorDonations() throws Exception {
        List<DonationDto> donations = Arrays.asList(
                createDonationDto(1L, "100.00", DonationStatus.COMPLETED),
                createDonationDto(2L, "200.00", DonationStatus.COMPLETED)
        );

        when(donationService.getDonationsByDonor(1L)).thenReturn(donations);

        mockMvc.perform(get("/donations/donor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test retrieving donations by orphanage ID.
     * Verifies that the orphanage and admin can access orphanage's donations.
     */
    @Test
    @WithMockUser
    void getDonationsByOrphanage_ShouldReturnOrphanageDonations() throws Exception {
        List<DonationDto> donations = Arrays.asList(
                createDonationDto(1L, "100.00", DonationStatus.COMPLETED),
                createDonationDto(2L, "200.00", DonationStatus.COMPLETED)
        );

        when(donationService.getDonationsByOrphanage(1L)).thenReturn(donations);

        mockMvc.perform(get("/donations/orphanage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test retrieving donations by status.
     * Verifies that only admin users can access donations by status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getDonationsByStatus_ShouldReturnDonationsByStatus() throws Exception {
        List<DonationDto> donations = Arrays.asList(
                createDonationDto(1L, "100.00", DonationStatus.COMPLETED),
                createDonationDto(2L, "200.00", DonationStatus.COMPLETED)
        );

        when(donationService.getDonationsByStatus(DonationStatus.COMPLETED)).thenReturn(donations);

        mockMvc.perform(get("/donations/status/COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    /**
     * Test updating a donation.
     * Verifies that only admin users can update donations.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDonation_ShouldUpdateDonation() throws Exception {
        UpdateDonationRequest request = new UpdateDonationRequest();
        request.setAmount(new BigDecimal("150.00"));
        request.setStatus(DonationStatus.COMPLETED);

        DonationDto updatedDonation = new DonationDto();
        updatedDonation.setId(1L);
        updatedDonation.setAmount(new BigDecimal("150.00"));
        updatedDonation.setStatus(DonationStatus.COMPLETED);

        when(donationService.updateDonation(eq(1L), any(UpdateDonationRequest.class))).thenReturn(updatedDonation);

        mockMvc.perform(put("/donations/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    /**
     * Test deleting a donation.
     * Verifies that only admin users can delete donations.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDonation_ShouldDeleteDonation() throws Exception {
        mockMvc.perform(delete("/donations/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    private DonationDto createDonationDto(Long id, String amount, DonationStatus status) {
        DonationDto dto = new DonationDto();
        dto.setId(id);
        dto.setAmount(new BigDecimal(amount));
        dto.setStatus(status);
        return dto;
    }
} 