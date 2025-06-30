package com.connectinghands.service;

import com.connectinghands.dto.CreateDonationRequest;
import com.connectinghands.dto.DonationDto;
import com.connectinghands.dto.UpdateDonationRequest;
import com.connectinghands.entity.DonationStatus;

import java.util.List;

/**
 * Service interface for managing donations.
 * Defines operations for creating, reading, updating, and deleting donations.
 *
 * @author Ragul Venkatesan
 */
public interface DonationService {
    /**
     * Creates a new donation.
     *
     * @param request The request containing donation details
     * @return The created donation DTO
     */
    DonationDto createDonation(CreateDonationRequest request);

    /**
     * Retrieves a donation by its ID.
     *
     * @param id The ID of the donation to retrieve
     * @return The donation DTO
     */
    DonationDto getDonation(Long id);

    /**
     * Retrieves all donations.
     *
     * @return A list of all donation DTOs
     */
    List<DonationDto> getAllDonations();

    /**
     * Retrieves all donations made by a specific donor.
     *
     * @param donorId The ID of the donor
     * @return A list of donation DTOs made by the donor
     */
    List<DonationDto> getDonationsByDonor(Long donorId);

    /**
     * Retrieves all donations received by a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return A list of donation DTOs received by the orphanage
     */
    List<DonationDto> getDonationsByOrphanage(Long orphanageId);

    /**
     * Retrieves all donations with a specific status.
     *
     * @param status The status to search for
     * @return A list of donation DTOs with the specified status
     */
    List<DonationDto> getDonationsByStatus(DonationStatus status);

    /**
     * Updates an existing donation.
     *
     * @param id The ID of the donation to update
     * @param request The request containing updated donation details
     * @return The updated donation DTO
     */
    DonationDto updateDonation(Long id, UpdateDonationRequest request);

    /**
     * Deletes a donation by its ID.
     *
     * @param id The ID of the donation to delete
     */
    void deleteDonation(Long id);
}
