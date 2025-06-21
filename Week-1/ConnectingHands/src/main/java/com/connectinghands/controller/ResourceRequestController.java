package com.connectinghands.controller;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.service.ResourceRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing resource requests.
 * Provides endpoints for CRUD operations with proper security constraints.
 *
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/resource-requests")
@RequiredArgsConstructor
public class ResourceRequestController {
    private final ResourceRequestService resourceRequestService;

    /**
     * Create a new resource request.
     * Requires authentication and orphanage user role.
     *
     * @param request The request containing resource request details
     * @return The created resource request DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ORPHANAGE')")
    public ResponseEntity<ResourceRequestDto> createResourceRequest(@Valid @RequestBody CreateResourceRequest request) {
        return ResponseEntity.ok(resourceRequestService.createResourceRequest(request));
    }

    /**
     * Get a resource request by ID.
     * Accessible to all authenticated users.
     *
     * @param id The ID of the resource request
     * @return The resource request DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResourceRequestDto> getResourceRequest(@PathVariable Long id) {
        return ResponseEntity.ok(resourceRequestService.getResourceRequest(id));
    }

    /**
     * Get all resource requests.
     * Requires ADMIN role.
     *
     * @return List of all resource request DTOs
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResourceRequestDto>> getAllResourceRequests() {
        return ResponseEntity.ok(resourceRequestService.getAllResourceRequests());
    }

    /**
     * Get all resource requests for a specific orphanage.
     * Accessible to the orphanage and ADMIN role.
     *
     * @param orphanageId The ID of the orphanage
     * @return List of resource request DTOs for the orphanage
     */
    @GetMapping("/orphanage/{orphanageId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrphanageUser(#orphanageId)")
    public ResponseEntity<List<ResourceRequestDto>> getResourceRequestsByOrphanage(@PathVariable Long orphanageId) {
        return ResponseEntity.ok(resourceRequestService.getResourceRequestsByOrphanage(orphanageId));
    }

    /**
     * Get all resource requests with a specific status.
     * Requires ADMIN role.
     *
     * @param status The status to filter by
     * @return List of resource request DTOs with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResourceRequestDto>> getResourceRequestsByStatus(@PathVariable ResourceRequestStatus status) {
        return ResponseEntity.ok(resourceRequestService.getResourceRequestsByStatus(status));
    }

    /**
     * Update an existing resource request.
     * Requires ADMIN role.
     *
     * @param id The ID of the resource request to update
     * @param request The request containing updated details
     * @return The updated resource request DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceRequestDto> updateResourceRequest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResourceRequest request) {
        return ResponseEntity.ok(resourceRequestService.updateResourceRequest(id, request));
    }

    /**
     * Delete a resource request.
     * Requires ADMIN role.
     *
     * @param id The ID of the resource request to delete
     * @return Empty response with OK status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteResourceRequest(@PathVariable Long id) {
        resourceRequestService.deleteResourceRequest(id);
        return ResponseEntity.ok().build();
    }
} 