package com.connectinghands.controller;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceDto;
import com.connectinghands.service.ResourceService;
import com.connectinghands.exception.DuplicateResourceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
     * Requires SYSTEM_ADMIN role.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody CreateResourceRequest request) {
        ResourceDto createdResource = resourceService.createResource(request);
        return ResponseEntity.created(null).body(createdResource);
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
    public ResponseEntity<Page<ResourceDto>> getAllResources(Pageable pageable) {
        return ResponseEntity.ok(resourceService.getAllResources(pageable));
    }

    /**
     * Updates an existing resource.
     * Requires SYSTEM_ADMIN role.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @Valid @RequestBody UpdateResourceDto request) {
        return ResponseEntity.ok(resourceService.updateResource(id, request));
    }

    /**
     * Deletes a resource by its ID.
     * Requires SYSTEM_ADMIN role.
     *
     * @param id The ID of the resource to delete
     * @return A response indicating success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok().build();
    }

    private void updateFieldIfPresent(Supplier<String> getter, Consumer<String> setter, Predicate<String> existsCheck, String errorMsg) {
        String value = getter.get();
        if (value != null) {
            if (existsCheck != null && existsCheck.test(value)) {
                throw new DuplicateResourceException(errorMsg);
            }
            setter.accept(value);
        }
    }
}