package com.connectinghands.repository;

import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing ResourceRequest entities.
 * Provides methods for CRUD operations and custom queries.
 *
 * @author Ragul Venkatesan
 */
@Repository
public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Long> {
    /**
     * Find all resource requests made by a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return List of resource requests made by the orphanage
     */
    List<ResourceRequest> findByOrphanageId(Long orphanageId);

    /**
     * Find all resource requests with a specific status.
     *
     * @param status The status to search for
     * @return List of resource requests with the specified status
     */
    List<ResourceRequest> findByStatus(ResourceRequestStatus status);

    /**
     * Find all resource requests fulfilled by a specific donor.
     *
     * @param donorId The ID of the donor
     * @return List of resource requests fulfilled by the donor
     */
    List<ResourceRequest> findByFulfilledBy(Long donorId);

    /**
     * Find all resource requests by an orphanage with a specific status.
     *
     * @param orphanageId The ID of the orphanage
     * @param status The status to search for
     * @return List of resource requests matching the criteria
     */
    List<ResourceRequest> findByOrphanageIdAndStatus(Long orphanageId, ResourceRequestStatus status);

    /**
     * Find all resource requests made by a specific orphanage within a date range.
     *
     * @param orphanageId The ID of the orphanage
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of resource requests made by the orphanage within the date range
     */
    List<ResourceRequest> findByOrphanageIdAndCreatedAtBetween(Long orphanageId, LocalDateTime startDate, LocalDateTime endDate);
} 