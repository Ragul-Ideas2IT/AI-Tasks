package com.connectinghands.service;

import com.connectinghands.dto.CreateDonationRequest;
import com.connectinghands.dto.DonationDto;
import com.connectinghands.dto.UpdateDonationRequest;
import com.connectinghands.entity.Donation;
import com.connectinghands.entity.DonationStatus;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.PaymentMethod;
import com.connectinghands.entity.User;
import com.connectinghands.repository.DonationRepository;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.impl.DonationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private OrphanageRepository orphanageRepository;
    
    @Mock
    private SecurityService securityService;
    
    @InjectMocks
    private DonationServiceImpl donationService;

    private User testUser;
    private Orphanage orphanage;
    private Donation donation;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        
        orphanage = new Orphanage();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        
        donation = new Donation();
        donation.setId(1L);
        donation.setDonor(testUser);
        donation.setOrphanage(orphanage);
        donation.setAmount(new BigDecimal("100.00"));
        donation.setCurrency("USD");
        donation.setStatus(DonationStatus.PENDING);
        donation.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        lenient().when(securityService.getCurrentUserId()).thenReturn(1L);
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    }

    @Test
    void createDonation_ValidRequest_ReturnsDonationDto() {
        CreateDonationRequest request = new CreateDonationRequest();
        request.setOrphanageId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setNotes("Test");

        when(orphanageRepository.findById(1L)).thenReturn(Optional.of(orphanage));
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);

        DonationDto dto = donationService.createDonation(request);
        assertThat(dto).isNotNull();
        assertThat(dto.getOrphanageId()).isEqualTo(1L);
        assertThat(dto.getAmount()).isEqualByComparingTo("100.00");
        assertThat(dto.getStatus()).isEqualTo(DonationStatus.PENDING);
    }

    @Test
    void createDonation_OrphanageNotFound_ThrowsException() {
        CreateDonationRequest request = new CreateDonationRequest();
        request.setOrphanageId(2L);
        when(orphanageRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> donationService.createDonation(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }

    @Test
    void getDonation_ValidId_ReturnsDonationDto() {
        when(donationRepository.findById(1L)).thenReturn(Optional.of(donation));
        DonationDto dto = donationService.getDonation(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getDonation_NotFound_ThrowsException() {
        when(donationRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> donationService.getDonation(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Donation not found");
    }

    @Test
    void getAllDonations_ReturnsList() {
        when(donationRepository.findAll()).thenReturn(Collections.singletonList(donation));
        List<DonationDto> list = donationService.getAllDonations();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getDonationsByDonor_ReturnsList() {
        when(donationRepository.findByDonorId(1L)).thenReturn(Collections.singletonList(donation));
        List<DonationDto> list = donationService.getDonationsByDonor(1L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDonorId()).isEqualTo(1L);
    }

    @Test
    void getDonationsByOrphanage_ReturnsList() {
        when(donationRepository.findByOrphanageId(1L)).thenReturn(Collections.singletonList(donation));
        List<DonationDto> list = donationService.getDonationsByOrphanage(1L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getOrphanageId()).isEqualTo(1L);
    }

    @Test
    void getDonationsByStatus_ReturnsList() {
        when(donationRepository.findByStatus(DonationStatus.PENDING)).thenReturn(Collections.singletonList(donation));
        List<DonationDto> list = donationService.getDonationsByStatus(DonationStatus.PENDING);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStatus()).isEqualTo(DonationStatus.PENDING);
    }

    @Test
    void updateDonation_ValidRequest_ReturnsUpdatedDto() {
        UpdateDonationRequest request = new UpdateDonationRequest();
        request.setAmount(new BigDecimal("200.00"));
        request.setStatus(DonationStatus.COMPLETED);
        when(donationRepository.findById(1L)).thenReturn(Optional.of(donation));
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);
        DonationDto dto = donationService.updateDonation(1L, request);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void updateDonation_NotFound_ThrowsException() {
        UpdateDonationRequest request = new UpdateDonationRequest();
        when(donationRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> donationService.updateDonation(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Donation not found");
    }

    @Test
    void deleteDonation_ValidId_DeletesDonation() {
        when(donationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(donationRepository).deleteById(1L);
        donationService.deleteDonation(1L);
        verify(donationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDonation_NotFound_ThrowsException() {
        when(donationRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> donationService.deleteDonation(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Donation not found");
    }
} 