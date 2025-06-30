package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.OrphanageStatus;
import com.connectinghands.entity.User;
import com.connectinghands.exception.DuplicateResourceException;
import com.connectinghands.exception.PermissionDeniedException;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.exception.ValidationException;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.OrphanageService;
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
 * Implementation of the OrphanageService interface.
 * Handles CRUD operations for orphanages.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class OrphanageServiceImpl implements OrphanageService {
    private final OrphanageRepository orphanageRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public OrphanageDto createOrphanage(CreateOrphanageRequest request) {
        // Validate unique constraints
        if (orphanageRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("An orphanage with this name already exists");
        }
        if (orphanageRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An orphanage with this email already exists");
        }
        if (orphanageRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("An orphanage with this phone number already exists");
        }

        // Get current user as admin
        Long currentUserId = securityService.getCurrentUserId();
        User admin = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create orphanage
        Orphanage orphanage = new Orphanage();
        orphanage.setName(request.getName());
        orphanage.setDescription(request.getDescription());
        orphanage.setAddress(request.getAddress());
        orphanage.setCity(request.getCity());
        orphanage.setState(request.getState());
        orphanage.setCountry(request.getCountry());
        orphanage.setPostalCode(request.getPostalCode());
        orphanage.setPhone(request.getPhone());
        orphanage.setEmail(request.getEmail());
        orphanage.setWebsite(request.getWebsite());
        orphanage.setCapacity(request.getCapacity());
        orphanage.setCurrentChildren(0);
        orphanage.setStatus(OrphanageStatus.PENDING);
        orphanage.setAdmin(admin);
        orphanage.setVerificationDocuments(request.getVerificationDocuments());

        orphanage = orphanageRepository.save(orphanage);

        // Log the creation
        auditLogService.logAction(
            "ORPHANAGE_CREATED",
            "Created orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    @Override
    @Transactional(readOnly = true)
    public OrphanageDto getOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));
        return mapToDto(orphanage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getAllOrphanages(Pageable pageable) {
        return orphanageRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesByStatus(OrphanageStatus status, Pageable pageable) {
        return orphanageRepository.findByStatus(status, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesByAdmin(Long adminId, Pageable pageable) {
        return orphanageRepository.findByAdminId(adminId, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesByCity(String city, Pageable pageable) {
        return orphanageRepository.findByCity(city, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesByState(String state, Pageable pageable) {
        return orphanageRepository.findByState(state, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesByCountry(String country, Pageable pageable) {
        return orphanageRepository.findByCountry(country, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> searchOrphanages(String searchTerm, Pageable pageable) {
        return orphanageRepository.searchOrphanages(searchTerm, pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrphanageDto> getOrphanagesWithAvailableCapacity(Pageable pageable) {
        return orphanageRepository.findOrphanagesWithAvailableCapacity(pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public OrphanageDto updateOrphanage(Long id, UpdateOrphanageRequest request) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        validateUpdatePermission(id);
        handleUniqueFields(request, orphanage);
        updateSimpleFields(request, orphanage);
        updateCapacity(request, orphanage);
        updateStatus(request, orphanage);

        orphanage = orphanageRepository.save(orphanage);
        logOrphanageUpdate(orphanage);
        return mapToDto(orphanage);
    }

    private void validateUpdatePermission(Long orphanageId) {
        if (!securityService.isCurrentUserOrphanageAdmin(orphanageId)) {
            throw new PermissionDeniedException("You don't have permission to update this orphanage");
        }
    }

    private void handleUniqueFields(UpdateOrphanageRequest request, Orphanage orphanage) {
        checkAndUpdateUniqueField(request.getName(), orphanage.getName(), orphanageRepository::existsByName, orphanage::setName, "An orphanage with this name already exists");
        checkAndUpdateUniqueField(request.getPhone(), orphanage.getPhone(), orphanageRepository::existsByPhone, orphanage::setPhone, "An orphanage with this phone number already exists");
        checkAndUpdateUniqueField(request.getEmail(), orphanage.getEmail(), orphanageRepository::existsByEmail, orphanage::setEmail, "An orphanage with this email already exists");
    }

    private void checkAndUpdateUniqueField(String newValue, String currentValue, java.util.function.Predicate<String> existsCheck, java.util.function.Consumer<String> setter, String errorMsg) {
        if (newValue != null && !newValue.equals(currentValue)) {
            if (existsCheck.test(newValue)) {
                throw new DuplicateResourceException(errorMsg);
            }
            setter.accept(newValue);
        }
    }

    private void updateSimpleFields(UpdateOrphanageRequest request, Orphanage orphanage) {
        setIfNotNull(request.getDescription(), orphanage::setDescription);
        setIfNotNull(request.getAddress(), orphanage::setAddress);
        setIfNotNull(request.getCity(), orphanage::setCity);
        setIfNotNull(request.getState(), orphanage::setState);
        setIfNotNull(request.getCountry(), orphanage::setCountry);
        setIfNotNull(request.getPostalCode(), orphanage::setPostalCode);
        setIfNotNull(request.getWebsite(), orphanage::setWebsite);
        setIfNotNull(request.getVerificationDocuments(), orphanage::setVerificationDocuments);
        setIfNotNull(request.getVerificationNotes(), orphanage::setVerificationNotes);
    }

    private void updateCapacity(UpdateOrphanageRequest request, Orphanage orphanage) {
        if (request.getCapacity() != null) {
            if (request.getCapacity() < orphanage.getCurrentChildren()) {
                throw new ValidationException("New capacity cannot be less than current number of children");
            }
            orphanage.setCapacity(request.getCapacity());
        }
    }

    private void updateStatus(UpdateOrphanageRequest request, Orphanage orphanage) {
        if (request.getStatus() != null) {
            orphanage.setStatus(OrphanageStatus.valueOf(request.getStatus()));
        }
    }

    private void logOrphanageUpdate(Orphanage orphanage) {
        auditLogService.logAction(
            "ORPHANAGE_UPDATED",
            "Updated orphanage: " + orphanage.getName(),
            orphanage.getId()
        );
    }

    // Helper methods
    private <T> void setIfNotNull(T value, java.util.function.Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }

    @Override
    @Transactional
    public void deleteOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        // Check if user has permission to delete
        if (!securityService.isCurrentUserOrphanageAdmin(id)) {
            throw new IllegalArgumentException("You don't have permission to delete this orphanage");
        }

        orphanageRepository.delete(orphanage);

        // Log the deletion
        auditLogService.logAction(
            "ORPHANAGE_DELETED",
            "Deleted orphanage: " + orphanage.getName(),
            orphanage.getId()
        );
    }

    @Override
    @Transactional
    public OrphanageDto verifyOrphanage(Long id, String notes) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        if (orphanage.getStatus() != OrphanageStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orphanages can be verified");
        }

        orphanage.setStatus(OrphanageStatus.ACTIVE);
        orphanage.setVerificationNotes(notes);
        orphanage.setVerifiedAt(LocalDateTime.now());
        orphanage.setVerifiedBy(securityService.getCurrentUserId());

        orphanage = orphanageRepository.save(orphanage);

        // Log the verification
        auditLogService.logAction(
            "ORPHANAGE_VERIFIED",
            "Verified orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    @Override
    @Transactional
    public OrphanageDto rejectOrphanage(Long id, String notes) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        if (orphanage.getStatus() != OrphanageStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orphanages can be rejected");
        }

        orphanage.setStatus(OrphanageStatus.REJECTED);
        orphanage.setVerificationNotes(notes);
        orphanage.setVerifiedAt(LocalDateTime.now());
        orphanage.setVerifiedBy(securityService.getCurrentUserId());

        orphanage = orphanageRepository.save(orphanage);

        // Log the rejection
        auditLogService.logAction(
            "ORPHANAGE_REJECTED",
            "Rejected orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    @Override
    @Transactional
    public OrphanageDto suspendOrphanage(Long id, String notes) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        if (orphanage.getStatus() != OrphanageStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active orphanages can be suspended");
        }

        orphanage.setStatus(OrphanageStatus.SUSPENDED);
        orphanage.setVerificationNotes(notes);
        orphanage.setVerifiedAt(LocalDateTime.now());
        orphanage.setVerifiedBy(securityService.getCurrentUserId());

        orphanage = orphanageRepository.save(orphanage);

        // Log the suspension
        auditLogService.logAction(
            "ORPHANAGE_SUSPENDED",
            "Suspended orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    @Override
    @Transactional
    public OrphanageDto reactivateOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        if (orphanage.getStatus() != OrphanageStatus.SUSPENDED) {
            throw new IllegalArgumentException("Only suspended orphanages can be reactivated");
        }

        orphanage.setStatus(OrphanageStatus.ACTIVE);
        orphanage.setVerifiedAt(LocalDateTime.now());
        orphanage.setVerifiedBy(securityService.getCurrentUserId());

        orphanage = orphanageRepository.save(orphanage);

        // Log the reactivation
        auditLogService.logAction(
            "ORPHANAGE_REACTIVATED",
            "Reactivated orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    @Override
    @Transactional
    public OrphanageDto closeOrphanage(Long id, String notes) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        if (orphanage.getStatus() == OrphanageStatus.CLOSED) {
            throw new IllegalArgumentException("Orphanage is already closed");
        }

        orphanage.setStatus(OrphanageStatus.CLOSED);
        orphanage.setVerificationNotes(notes);
        orphanage.setVerifiedAt(LocalDateTime.now());
        orphanage.setVerifiedBy(securityService.getCurrentUserId());

        orphanage = orphanageRepository.save(orphanage);

        // Log the closure
        auditLogService.logAction(
            "ORPHANAGE_CLOSED",
            "Closed orphanage: " + orphanage.getName(),
            orphanage.getId()
        );

        return mapToDto(orphanage);
    }

    private OrphanageDto mapToDto(Orphanage orphanage) {
        OrphanageDto dto = new OrphanageDto();
        dto.setId(orphanage.getId());
        dto.setName(orphanage.getName());
        dto.setDescription(orphanage.getDescription());
        dto.setAddress(orphanage.getAddress());
        dto.setCity(orphanage.getCity());
        dto.setState(orphanage.getState());
        dto.setCountry(orphanage.getCountry());
        dto.setPostalCode(orphanage.getPostalCode());
        dto.setPhone(orphanage.getPhone());
        dto.setEmail(orphanage.getEmail());
        dto.setWebsite(orphanage.getWebsite());
        dto.setCapacity(orphanage.getCapacity());
        dto.setCurrentChildren(orphanage.getCurrentChildren());
        dto.setStatus(orphanage.getStatus());
        dto.setAdminId(orphanage.getAdmin().getId());
        dto.setAdminName(orphanage.getAdmin().getFirstName() + " " + orphanage.getAdmin().getLastName());
        dto.setVerificationDocuments(orphanage.getVerificationDocuments());
        dto.setVerificationNotes(orphanage.getVerificationNotes());
        dto.setVerifiedAt(orphanage.getVerifiedAt());
        dto.setVerifiedBy(orphanage.getVerifiedBy());
        dto.setCreatedAt(orphanage.getCreatedAt());
        dto.setUpdatedAt(orphanage.getUpdatedAt());
        return dto;
    }
}
