package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRequestRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.ResourceRequestService;
import com.connectinghands.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ResourceRequestService.
 * Handles CRUD operations for resource requests with audit logging.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {
    private final ResourceRequestRepository resourceRequestRepository;
    private final OrphanageRepository orphanageRepository;
    private final SecurityService securityService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public ResourceRequestDto createResourceRequest(CreateResourceRequest request) {
        Orphanage orphanage = orphanageRepository.findById(request.getOrphanageId())
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setName(request.getName());
        resourceRequest.setDescription(request.getDescription());
        resourceRequest.setCategory(ResourceCategory.valueOf(request.getCategory()));
        resourceRequest.setQuantity(request.getQuantity());
        resourceRequest.setUnit(request.getUnit());
        resourceRequest.setOrphanage(orphanage);
        resourceRequest.setStatus(ResourceRequestStatus.PENDING);

        ResourceRequest savedRequest = resourceRequestRepository.save(resourceRequest);
        
        auditLogService.logAction(
            "CREATE_RESOURCE_REQUEST",
            "Created resource request: " + savedRequest.getId(),
            savedRequest.getId()
        );

        return convertToDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceRequestDto getResourceRequest(Long id) {
        ResourceRequest resourceRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found"));

        return convertToDto(resourceRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getAllResourceRequests() {
        return resourceRequestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getResourceRequestsByOrphanage(Long orphanageId) {
        return resourceRequestRepository.findByOrphanageId(orphanageId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getResourceRequestsByStatus(ResourceRequestStatus status) {
        return resourceRequestRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResourceRequestDto updateResourceRequest(Long id, UpdateResourceRequest request) {
        ResourceRequest resourceRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found"));

        if (request.getName() != null) {
            resourceRequest.setName(request.getName());
        }
        if (request.getDescription() != null) {
            resourceRequest.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            resourceRequest.setCategory(ResourceCategory.valueOf(request.getCategory()));
        }
        if (request.getQuantity() != null) {
            resourceRequest.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            resourceRequest.setUnit(request.getUnit());
        }
        if (request.getStatus() != null) {
            resourceRequest.setStatus(ResourceRequestStatus.valueOf(request.getStatus()));
            if (request.getStatus() == ResourceRequestStatus.FULFILLED) {
                resourceRequest.setFulfilledBy(securityService.getCurrentUserId());
                resourceRequest.setFulfilledAt(LocalDateTime.now());
            }
        }

        ResourceRequest updatedRequest = resourceRequestRepository.save(resourceRequest);
        
        auditLogService.logAction(
            "UPDATE_RESOURCE_REQUEST",
            "Updated resource request: " + id,
            id
        );

        return convertToDto(updatedRequest);
    }

    @Override
    @Transactional
    public void deleteResourceRequest(Long id) {
        if (!resourceRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource request not found");
        }

        resourceRequestRepository.deleteById(id);
        
        auditLogService.logAction(
            "DELETE_RESOURCE_REQUEST",
            "Deleted resource request: " + id,
            id
        );
    }

    private ResourceRequestDto convertToDto(ResourceRequest request) {
        ResourceRequestDto dto = new ResourceRequestDto();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setCategory(request.getCategory());
        dto.setQuantity(request.getQuantity());
        dto.setUnit(request.getUnit());
        dto.setOrphanageName(request.getOrphanage().getName());
        dto.setOrphanageId(request.getOrphanage().getId());
        dto.setStatus(request.getStatus());
        dto.setFulfilledBy(request.getFulfilledBy());
        dto.setFulfilledAt(request.getFulfilledAt());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
} 