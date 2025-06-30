package com.connectinghands.controller;

import com.connectinghands.dto.CreateDonationRequest;
import com.connectinghands.dto.DonationDto;
import com.connectinghands.dto.UpdateDonationRequest;
import com.connectinghands.entity.DonationStatus;
import com.connectinghands.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing donations.
 * Provides endpoints for CRUD operations on donations with proper security constraints.
 *
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;

    /**
     * Creates a new donation.
     * Requires authentication.
     *
     * @param request The request containing donation details
     * @return The created donation DTO
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DonationDto> createDonation(@Valid @RequestBody CreateDonationRequest request) {
        return ResponseEntity.ok(donationService.createDonation(request));
    }

    /**
     * Retrieves a donation by its ID.
     * Accessible to all authenticated users.
     *
     * @param id The ID of the donation
     * @return The donation DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DonationDto> getDonation(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonation(id));
    }

    /**
     * Retrieves all donations.
     * Requires ADMIN role.
     *
     * @return A list of donation DTOs
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DonationDto>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }

    /**
     * Retrieves all donations made by a specific donor.
     * Accessible to the donor and ADMIN role.
     *
     * @param donorId The ID of the donor
     * @return A list of donation DTOs made by the donor
     */
    @GetMapping("/donor/{donorId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#donorId)")
    public ResponseEntity<List<DonationDto>> getDonationsByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(donationService.getDonationsByDonor(donorId));
    }

    /**
     * Retrieves all donations received by a specific orphanage.
     * Accessible to the orphanage and ADMIN role.
     *
     * @param orphanageId The ID of the orphanage
     * @return A list of donation DTOs received by the orphanage
     */
    @GetMapping("/orphanage/{orphanageId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrphanageUser(#orphanageId)")
    public ResponseEntity<List<DonationDto>> getDonationsByOrphanage(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(donationService.getDonationsByOrphanage(orphanageId));
    }

    /**
     * Retrieves all donations with a specific status.
     * Requires ADMIN role.
     *
     * @param status The status to search for
     * @return A list of donation DTOs with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DonationDto>> getDonationsByStatus(@PathVariable DonationStatus status) {
        return ResponseEntity.ok(donationService.getDonationsByStatus(status));
    }

    /**
     * Updates an existing donation.
     * Requires ADMIN role.
     *
     * @param id The ID of the donation to update
     * @param request The request containing updated donation details
     * @return The updated donation DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonationDto> updateDonation(@PathVariable Long id, @Valid @RequestBody UpdateDonationRequest request) {
        return ResponseEntity.ok(donationService.updateDonation(id, request));
    }

    /**
     * Deletes a donation by its ID.
     * Requires ADMIN role.
     *
     * @param id The ID of the donation to delete
     * @return A response indicating success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.ok().build();
    }
}
