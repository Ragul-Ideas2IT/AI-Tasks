package com.connectinghands.repository;

import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.OrphanageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Orphanage entities.
 * 
 * @author Ragul Venkatesan
 */
@Repository
public interface OrphanageRepository extends JpaRepository<Orphanage, Long> {
    /**
     * Find an orphanage by its name.
     *
     * @param name the name of the orphanage
     * @return an Optional containing the orphanage if found
     */
    Optional<Orphanage> findByName(String name);

    /**
     * Find orphanages by their status.
     *
     * @param status the status to filter by
     * @param pageable pagination information
     * @return a page of orphanages with the specified status
     */
    Page<Orphanage> findByStatus(OrphanageStatus status, Pageable pageable);

    /**
     * Find orphanages by their admin user ID.
     *
     * @param adminId the ID of the admin user
     * @param pageable pagination information
     * @return a page of orphanages managed by the specified admin
     */
    Page<Orphanage> findByAdminId(Long adminId, Pageable pageable);

    /**
     * Find orphanages by city.
     *
     * @param city the city to search in
     * @param pageable pagination information
     * @return a page of orphanages in the specified city
     */
    Page<Orphanage> findByCity(String city, Pageable pageable);

    /**
     * Find orphanages by state.
     *
     * @param state the state to search in
     * @param pageable pagination information
     * @return a page of orphanages in the specified state
     */
    Page<Orphanage> findByState(String state, Pageable pageable);

    /**
     * Find orphanages by country.
     *
     * @param country the country to search in
     * @param pageable pagination information
     * @return a page of orphanages in the specified country
     */
    Page<Orphanage> findByCountry(String country, Pageable pageable);

    /**
     * Search orphanages by name, city, state, or country.
     *
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of orphanages matching the search criteria
     */
    @Query("SELECT o FROM Orphanage o WHERE " +
           "LOWER(o.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(o.country) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Orphanage> searchOrphanages(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find orphanages with available capacity.
     *
     * @param pageable pagination information
     * @return a page of orphanages that have available capacity
     */
    @Query("SELECT o FROM Orphanage o WHERE o.currentChildren < o.capacity")
    Page<Orphanage> findOrphanagesWithAvailableCapacity(Pageable pageable);

    /**
     * Check if an orphanage exists with the given name.
     *
     * @param name the name to check
     * @return true if an orphanage exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Check if an orphanage exists with the given email.
     *
     * @param email the email to check
     * @return true if an orphanage exists with the given email
     */
    boolean existsByEmail(String email);

    /**
     * Check if an orphanage exists with the given phone number.
     *
     * @param phone the phone number to check
     * @return true if an orphanage exists with the given phone number
     */
    boolean existsByPhone(String phone);
} 