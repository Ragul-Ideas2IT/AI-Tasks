package com.connectinghands.controller;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing resources.
 * Provides endpoints for CRUD operations on resources with proper security constraints.
 *
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    /**
     * Creates a new resource.
     * Requires ADMIN role.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody CreateResourceRequest request) {
        return ResponseEntity.ok(resourceService.createResource(request));
    }

    /**
     * Retrieves a resource by its ID.
     * Accessible to all authenticated users.
     *
     * @param id The ID of the resource
     * @return The resource DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.getResource(id));
    }

    /**
     * Retrieves all resources.
     * Accessible to all authenticated users.
     *
     * @return A list of resource DTOs
     */
    @GetMapping
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    /**
     * Updates an existing resource.
     * Requires ADMIN role.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @Valid @RequestBody UpdateResourceRequest request) {
        return ResponseEntity.ok(resourceService.updateResource(id, request));
    }

    /**
     * Deletes a resource by its ID.
     * Requires ADMIN role.
     *
     * @param id The ID of the resource to delete
     * @return A response indicating success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok().build();
    }
} 