package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceDto;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
     * @return A list of resource DTOs
     */
    List<ResourceDto> getResourcesByOrphanage(Long orphanageId);

    /**
     * Retrieves resources by category.
     *
     * @param category The category of the resources
     * @return A list of resource DTOs
     */
    List<ResourceDto> getResourcesByCategory(ResourceCategory category);

    /**
     * Retrieves resources by status.
     *
     * @param status The status of the resources
     * @return A list of resource DTOs
     */
    List<ResourceDto> getResourcesByStatus(ResourceStatus status);

    /**
     * Updates an existing resource.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     */
    ResourceDto updateResource(Long id, UpdateResourceDto request);

    /**
     * Deletes a resource by its ID.
     *
     * @param id The ID of the resource to delete
     */
    void deleteResource(Long id);

    /**
     * Retrieves all resources with pagination.
     *
     * @param pageable The pageable object
     * @return A page of resource DTOs
     */
    Page<ResourceDto> getAllResources(Pageable pageable);

    /**
     * Retrieves all resources with their orphanages to avoid N+1 queries.
     *
     * @return A list of resource DTOs
     */
    List<ResourceDto> getAllResourcesWithOrphanage();
}
