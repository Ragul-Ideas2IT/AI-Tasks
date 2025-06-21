package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateDonationRequest;
import com.connectinghands.dto.DonationDto;
import com.connectinghands.dto.UpdateDonationRequest;
import com.connectinghands.entity.Donation;
import com.connectinghands.entity.DonationStatus;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.User;
import com.connectinghands.repository.DonationRepository;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.DonationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the DonationService interface.
 * Handles CRUD operations for donations.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final OrphanageRepository orphanageRepository;

    /**
     * Creates a new donation.
     * Logs the creation action and sets initial status to PENDING.
     *
     * @param request The request containing donation details
     * @return The created donation DTO
     * @throws EntityNotFoundException if the orphanage is not found
     */
    @Override
    @Transactional
    public DonationDto createDonation(CreateDonationRequest request) {
        Orphanage orphanage = orphanageRepository.findById(request.getOrphanageId())
                .orElseThrow(() -> new EntityNotFoundException("Orphanage not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User donor = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Donor not found"));

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setOrphanage(orphanage);
        donation.setAmount(request.getAmount());
        donation.setCurrency(request.getCurrency());
        donation.setStatus(DonationStatus.PENDING);
        donation.setPaymentMethod(request.getPaymentMethod());
        donation.setNotes(request.getNotes());

        Donation savedDonation = donationRepository.save(donation);
        return mapToDto(savedDonation);
    }

    /**
     * Retrieves a donation by its ID.
     *
     * @param id The ID of the donation to retrieve
     * @return The donation DTO
     * @throws EntityNotFoundException if the donation is not found
     */
    @Override
    @Transactional(readOnly = true)
    public DonationDto getDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));
        return mapToDto(donation);
    }

    /**
     * Retrieves all donations.
     *
     * @return A list of all donation DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<DonationDto> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all donations made by a specific donor.
     *
     * @param donorId The ID of the donor
     * @return A list of donation DTOs made by the donor
     */
    @Override
    @Transactional(readOnly = true)
    public List<DonationDto> getDonationsByDonor(Long donorId) {
        return donationRepository.findByDonorId(donorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all donations received by a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return A list of donation DTOs received by the orphanage
     */
    @Override
    @Transactional(readOnly = true)
    public List<DonationDto> getDonationsByOrphanage(Long orphanageId) {
        return donationRepository.findByOrphanageId(orphanageId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all donations with a specific status.
     *
     * @param status The status to search for
     * @return A list of donation DTOs with the specified status
     */
    @Override
    @Transactional(readOnly = true)
    public List<DonationDto> getDonationsByStatus(DonationStatus status) {
        return donationRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing donation.
     * Logs the update action.
     *
     * @param id The ID of the donation to update
     * @param request The request containing updated donation details
     * @return The updated donation DTO
     * @throws EntityNotFoundException if the donation is not found
     */
    @Override
    @Transactional
    public DonationDto updateDonation(Long id, UpdateDonationRequest request) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));

        if (request.getAmount() != null) {
            donation.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            donation.setCurrency(request.getCurrency());
        }
        if (request.getStatus() != null) {
            donation.setStatus(request.getStatus());
        }
        if (request.getPaymentMethod() != null) {
            donation.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getNotes() != null) {
            donation.setNotes(request.getNotes());
        }

        Donation updatedDonation = donationRepository.save(donation);
        return mapToDto(updatedDonation);
    }

    /**
     * Deletes a donation by its ID.
     * Logs the deletion action.
     *
     * @param id The ID of the donation to delete
     * @throws EntityNotFoundException if the donation is not found
     */
    @Override
    @Transactional
    public void deleteDonation(Long id) {
        if (!donationRepository.existsById(id)) {
            throw new EntityNotFoundException("Donation not found");
        }
        donationRepository.deleteById(id);
    }

    /**
     * Converts a Donation entity to a DonationDto.
     *
     * @param donation The donation entity to convert
     * @return The converted DonationDto
     */
    private DonationDto mapToDto(Donation donation) {
        DonationDto dto = new DonationDto();
        dto.setId(donation.getId());
        dto.setDonorId(donation.getDonor().getId());
        dto.setOrphanageId(donation.getOrphanage().getId());
        dto.setAmount(donation.getAmount());
        dto.setCurrency(donation.getCurrency());
        dto.setStatus(donation.getStatus());
        dto.setPaymentMethod(donation.getPaymentMethod());
        dto.setTransactionId(donation.getTransactionId());
        dto.setNotes(donation.getNotes());
        return dto;
    }
} 