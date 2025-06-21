package com.connectinghands.service;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.OrphanageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing orphanages.
 * 
 * @author Ragul Venkatesan
 */
public interface OrphanageService {
    /**
     * Create a new orphanage.
     *
     * @param request the orphanage creation request
     * @return the created orphanage DTO
     */
    OrphanageDto createOrphanage(CreateOrphanageRequest request);

    /**
     * Get an orphanage by its ID.
     *
     * @param id the orphanage ID
     * @return the orphanage DTO
     */
    OrphanageDto getOrphanage(Long id);

    /**
     * Get all orphanages with pagination.
     *
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getAllOrphanages(Pageable pageable);

    /**
     * Get orphanages by status with pagination.
     *
     * @param status the status to filter by
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesByStatus(OrphanageStatus status, Pageable pageable);

    /**
     * Get orphanages by admin ID.
     *
     * @param adminId the admin user ID
     * @return a list of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesByAdmin(Long adminId, Pageable pageable);

    /**
     * Get orphanages by city with pagination.
     *
     * @param city the city to search in
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesByCity(String city, Pageable pageable);

    /**
     * Get orphanages by state with pagination.
     *
     * @param state the state to search in
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesByState(String state, Pageable pageable);

    /**
     * Get orphanages by country with pagination.
     *
     * @param country the country to search in
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesByCountry(String country, Pageable pageable);

    /**
     * Search orphanages by name, city, state, or country.
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> searchOrphanages(String searchTerm, Pageable pageable);

    /**
     * Get orphanages with available capacity.
     *
     * @param pageable pagination information
     * @return a page of orphanage DTOs
     */
    Page<OrphanageDto> getOrphanagesWithAvailableCapacity(Pageable pageable);

    /**
     * Update an existing orphanage.
     *
     * @param id the orphanage ID
     * @param request the update request
     * @return the updated orphanage DTO
     */
    OrphanageDto updateOrphanage(Long id, UpdateOrphanageRequest request);

    /**
     * Delete an orphanage.
     *
     * @param id the orphanage ID
     */
    void deleteOrphanage(Long id);

    /**
     * Verify an orphanage.
     *
     * @param id the orphanage ID
     * @param notes verification notes
     * @return the verified orphanage DTO
     */
    OrphanageDto verifyOrphanage(Long id, String notes);

    /**
     * Reject an orphanage.
     *
     * @param id the orphanage ID
     * @param notes rejection notes
     * @return the rejected orphanage DTO
     */
    OrphanageDto rejectOrphanage(Long id, String notes);

    /**
     * Suspend an orphanage.
     *
     * @param id the orphanage ID
     * @param notes suspension notes
     * @return the suspended orphanage DTO
     */
    OrphanageDto suspendOrphanage(Long id, String notes);

    /**
     * Reactivate a suspended orphanage.
     *
     * @param id the orphanage ID
     * @return the reactivated orphanage DTO
     */
    OrphanageDto reactivateOrphanage(Long id);

    /**
     * Close an orphanage.
     *
     * @param id the orphanage ID
     * @param notes closure notes
     * @return the closed orphanage DTO
     */
    OrphanageDto closeOrphanage(Long id, String notes);
} 