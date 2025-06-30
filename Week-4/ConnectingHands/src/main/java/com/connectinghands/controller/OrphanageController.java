package com.connectinghands.controller;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.OrphanageStatus;
import com.connectinghands.service.OrphanageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing orphanages.
 * 
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/orphanages")
@RequiredArgsConstructor
public class OrphanageController {
    private final OrphanageService orphanageService;

    /**
     * Create a new orphanage.
     * Only users with ORPHANAGE role can create orphanages.
     *
     * @param request the orphanage creation request
     * @return the created orphanage
     */
    @PostMapping
    @PreAuthorize("hasRole('ORPHANAGE')")
    public ResponseEntity<OrphanageDto> createOrphanage(@Valid @RequestBody CreateOrphanageRequest request) {
        return new ResponseEntity<>(orphanageService.createOrphanage(request), HttpStatus.CREATED);
    }

    /**
     * Get an orphanage by its ID.
     * Any authenticated user can access this endpoint.
     *
     * @param id the orphanage ID
     * @return the orphanage
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrphanageDto> getOrphanage(@PathVariable Long id) {
        return ResponseEntity.ok(orphanageService.getOrphanage(id));
    }

    /**
     * Get all orphanages with pagination.
     * Only users with ADMIN role can access this endpoint.
     *
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrphanageDto>> getAllOrphanages(Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getAllOrphanages(pageable));
    }

    /**
     * Get orphanages by status with pagination.
     * Only users with ADMIN role can access this endpoint.
     *
     * @param status the status to filter by
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesByStatus(
            @PathVariable OrphanageStatus status, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesByStatus(status, pageable));
    }

    /**
     * Get orphanages by admin ID with pagination.
     * Only users with ADMIN role can access this endpoint.
     *
     * @param adminId the admin user ID
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesByAdmin(
            @PathVariable Long adminId, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesByAdmin(adminId, pageable));
    }

    /**
     * Get orphanages by city with pagination.
     * Any authenticated user can access this endpoint.
     *
     * @param city the city to search in
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/city/{city}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesByCity(
            @PathVariable String city, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesByCity(city, pageable));
    }

    /**
     * Get orphanages by state with pagination.
     * Any authenticated user can access this endpoint.
     *
     * @param state the state to search in
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/state/{state}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesByState(
            @PathVariable String state, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesByState(state, pageable));
    }

    /**
     * Get orphanages by country with pagination.
     * Any authenticated user can access this endpoint.
     *
     * @param country the country to search in
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/country/{country}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesByCountry(
            @PathVariable String country, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesByCountry(country, pageable));
    }

    /**
     * Search orphanages by name, city, state, or country.
     * Any authenticated user can access this endpoint.
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrphanageDto>> searchOrphanages(
            @RequestParam String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(orphanageService.searchOrphanages(searchTerm, pageable));
    }

    /**
     * Get orphanages with available capacity.
     * Any authenticated user can access this endpoint.
     *
     * @param pageable pagination information
     * @return a page of orphanages
     */
    @GetMapping("/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrphanageDto>> getOrphanagesWithAvailableCapacity(Pageable pageable) {
        return ResponseEntity.ok(orphanageService.getOrphanagesWithAvailableCapacity(pageable));
    }

    /**
     * Update an existing orphanage.
     * Only the orphanage admin or users with ADMIN role can update an orphanage.
     *
     * @param id the orphanage ID
     * @param request the update request
     * @return the updated orphanage
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserOrphanageAdmin(#id)")
    public ResponseEntity<OrphanageDto> updateOrphanage(
            @PathVariable Long id, @Valid @RequestBody UpdateOrphanageRequest request) {
        return ResponseEntity.ok(orphanageService.updateOrphanage(id, request));
    }

    /**
     * Delete an orphanage.
     * Only users with ADMIN role can delete an orphanage.
     *
     * @param id the orphanage ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrphanage(@PathVariable Long id) {
        orphanageService.deleteOrphanage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verify an orphanage.
     * Only users with ADMIN role can verify an orphanage.
     *
     * @param id the orphanage ID
     * @param notes verification notes
     * @return the verified orphanage
     */
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> verifyOrphanage(
            @PathVariable Long id, @RequestParam String notes) {
        return ResponseEntity.ok(orphanageService.verifyOrphanage(id, notes));
    }

    /**
     * Reject an orphanage.
     * Only users with ADMIN role can reject an orphanage.
     *
     * @param id the orphanage ID
     * @param notes rejection notes
     * @return the rejected orphanage
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> rejectOrphanage(
            @PathVariable Long id, @RequestParam String notes) {
        return ResponseEntity.ok(orphanageService.rejectOrphanage(id, notes));
    }

    /**
     * Suspend an orphanage.
     * Only users with ADMIN role can suspend an orphanage.
     *
     * @param id the orphanage ID
     * @param notes suspension notes
     * @return the suspended orphanage
     */
    @PostMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> suspendOrphanage(
            @PathVariable Long id, @RequestParam String notes) {
        return ResponseEntity.ok(orphanageService.suspendOrphanage(id, notes));
    }

    /**
     * Reactivate a suspended orphanage.
     * Only users with ADMIN role can reactivate an orphanage.
     *
     * @param id the orphanage ID
     * @return the reactivated orphanage
     */
    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> reactivateOrphanage(@PathVariable Long id) {
        return ResponseEntity.ok(orphanageService.reactivateOrphanage(id));
    }

    /**
     * Close an orphanage.
     * Only users with ADMIN role can close an orphanage.
     *
     * @param id the orphanage ID
     * @param notes closure notes
     * @return the closed orphanage
     */
    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> closeOrphanage(
            @PathVariable Long id, @RequestParam String notes) {
        return ResponseEntity.ok(orphanageService.closeOrphanage(id, notes));
    }
}
