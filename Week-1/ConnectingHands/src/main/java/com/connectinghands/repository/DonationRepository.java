package com.connectinghands.repository;

import com.connectinghands.entity.Donation;
import com.connectinghands.entity.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing Donation entities.
 * Provides CRUD operations and custom queries for donations.
 *
 * @author Ragul Venkatesan
 */
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    /**
     * Finds all donations made by a specific donor.
     *
     * @param donorId The ID of the donor
     * @return List of donations made by the donor
     */
    List<Donation> findByDonorId(Long donorId);

    /**
     * Finds all donations received by a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return List of donations received by the orphanage
     */
    List<Donation> findByOrphanageId(Long orphanageId);

    /**
     * Finds all donations with a specific status.
     *
     * @param status The status to search for
     * @return List of donations with the specified status
     */
    List<Donation> findByStatus(DonationStatus status);

    /**
     * Finds all donations received by a specific orphanage within a date range.
     *
     * @param orphanageId The ID of the orphanage
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of donations received by the orphanage within the date range
     */
    List<Donation> findByOrphanageIdAndCreatedAtBetween(Long orphanageId, LocalDateTime startDate, LocalDateTime endDate);
} 