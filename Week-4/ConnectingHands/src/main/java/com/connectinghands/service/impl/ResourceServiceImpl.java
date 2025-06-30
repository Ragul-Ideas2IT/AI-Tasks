package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceDto;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRepository;
import com.connectinghands.service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ResourceService interface.
 * Handles CRUD operations for resources.
 *
 * @author Ragul Venkatesan
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final OrphanageRepository orphanageRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, OrphanageRepository orphanageRepository) {
        this.resourceRepository = resourceRepository;
        this.orphanageRepository = orphanageRepository;
    }

    /**
     * Creates a new resource.
     * Logs the creation action and sets initial status to AVAILABLE.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     * @throws ResourceNotFoundException if the orphanage is not found
     */
    @Override
    @Transactional
    public ResourceDto createResource(CreateResourceRequest request) {
        Orphanage orphanage = orphanageRepository.findById(request.getOrphanageId())
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setCategory(ResourceCategory.valueOf(request.getCategory()));
        resource.setQuantity(request.getQuantity());
        resource.setUnit(request.getUnit());
        resource.setOrphanage(orphanage);
        resource.setStatus(ResourceStatus.AVAILABLE);

        Resource savedResource = resourceRepository.save(resource);
        return convertToDto(savedResource);
    }

    /**
     * Retrieves a resource by its ID.
     *
     * @param id The ID of the resource to retrieve
     * @return The resource DTO
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Override
    @Transactional(readOnly = true)
    public ResourceDto getResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return convertToDto(resource);
    }

    /**
     * Retrieves all resources with pagination support.
     * Uses Spring Data's Pageable to efficiently handle large datasets.
     *
     * @param pageable the pagination and sorting information
     * @return a page of resource DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceDto> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    /**
     * Retrieves resources by orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return A list of resource DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> getResourcesByOrphanage(Long orphanageId) {
        return resourceRepository.findByOrphanageId(orphanageId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves resources by category.
     *
     * @param category The category of the resources
     * @return A list of resource DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> getResourcesByCategory(ResourceCategory category) {
        return resourceRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves resources by status.
     *
     * @param status The status of the resources
     * @return A list of resource DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> getResourcesByStatus(ResourceStatus status) {
        return resourceRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all resources with their orphanages in a single query to avoid the N+1 problem.
     *
     * @return a list of resource DTOs with orphanage data eagerly loaded
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> getAllResourcesWithOrphanage() {
        return resourceRepository.findAllWithOrphanage().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing resource.
     * Logs the update action.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Override
    @Transactional
    public ResourceDto updateResource(Long id, UpdateResourceDto request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        
        if (request.getName() != null) {
            resource.setName(request.getName());
        }
        if (request.getDescription() != null) {
            resource.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            resource.setCategory(request.getCategory());
        }
        if (request.getQuantity() != null) {
            resource.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            resource.setUnit(request.getUnit());
        }
        if (request.getStatus() != null) {
            resource.setStatus(request.getStatus());
        }
        
        Resource updatedResource = resourceRepository.save(resource);
        return convertToDto(updatedResource);
    }

    /**
     * Deletes a resource by its ID.
     * Logs the deletion action.
     *
     * @param id The ID of the resource to delete
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Override
    @Transactional
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        resourceRepository.deleteById(id);
    }

    /**
     * Converts a Resource entity to a ResourceDto.
     *
     * @param resource The resource entity to convert
     * @return The converted ResourceDto
     */
    private ResourceDto convertToDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setDescription(resource.getDescription());
        dto.setCategory(resource.getCategory());
        dto.setQuantity(resource.getQuantity());
        dto.setUnit(resource.getUnit());
        dto.setOrphanageName(resource.getOrphanage().getName());
        dto.setStatus(resource.getStatus());
        dto.setCreatedAt(resource.getCreatedAt());
        dto.setUpdatedAt(resource.getUpdatedAt());
        return dto;
    }
}