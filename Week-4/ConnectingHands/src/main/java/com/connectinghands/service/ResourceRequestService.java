package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;

import java.util.List;

/**
 * Service interface for managing resource requests.
 * Defines CRUD operations and queries for resource requests.
 *
 * @author Ragul Venkatesan
 */
public interface ResourceRequestService {
    /**
     * Create a new resource request.
     *
     * @param request The request data for creating a resource request
     * @return The created resource request DTO
     */
    ResourceRequestDto createResourceRequest(CreateResourceRequest request);

    /**
     * Get a resource request by its ID.
     *
     * @param id The ID of the resource request
     * @return The resource request DTO
     */
    ResourceRequestDto getResourceRequest(Long id);

    /**
     * Get all resource requests.
     *
     * @return List of all resource request DTOs
     */
    List<ResourceRequestDto> getAllResourceRequests();

    /**
     * Get all resource requests for a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return List of resource request DTOs for the orphanage
     */
    List<ResourceRequestDto> getResourceRequestsByOrphanage(Long orphanageId);

    /**
     * Get all resource requests with a specific status.
     *
     * @param status The status to filter by
     * @return List of resource request DTOs with the specified status
     */
    List<ResourceRequestDto> getResourceRequestsByStatus(ResourceRequestStatus status);

    /**
     * Update an existing resource request.
     *
     * @param id The ID of the resource request to update
     * @param request The request data for updating the resource request
     * @return The updated resource request DTO
     */
    ResourceRequestDto updateResourceRequest(Long id, UpdateResourceRequest request);

    /**
     * Delete a resource request by its ID.
     *
     * @param id The ID of the resource request to delete
     */
    void deleteResourceRequest(Long id);
}
