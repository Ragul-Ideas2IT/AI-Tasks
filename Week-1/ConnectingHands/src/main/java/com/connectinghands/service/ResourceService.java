package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing resources.
 * Defines operations for creating, reading, updating, and deleting resources.
 *
 * @author Ragul Venkatesan
 */
public interface ResourceService {
    /**
     * Creates a new resource.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     */
    ResourceDto createResource(CreateResourceRequest request);

    /**
     * Retrieves a resource by its ID.
     *
     * @param id The ID of the resource to retrieve
     * @return The resource DTO
     */
    ResourceDto getResource(Long id);

    /**
     * Retrieves resources by orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @param pageable The pageable object
     * @return A page of resource DTOs
     */
    Page<ResourceDto> getResourcesByOrphanage(Long orphanageId, Pageable pageable);

    /**
     * Retrieves resources by category.
     *
     * @param category The category of the resources
     * @param pageable The pageable object
     * @return A page of resource DTOs
     */
    Page<ResourceDto> getResourcesByCategory(ResourceCategory category, Pageable pageable);

    /**
     * Retrieves resources by status.
     *
     * @param status The status of the resources
     * @param pageable The pageable object
     * @return A page of resource DTOs
     */
    Page<ResourceDto> getResourcesByStatus(ResourceStatus status, Pageable pageable);

    /**
     * Updates an existing resource.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     */
    ResourceDto updateResource(Long id, UpdateResourceRequest request);

    /**
     * Deletes a resource by its ID.
     *
     * @param id The ID of the resource to delete
     */
    void deleteResource(Long id);
} 