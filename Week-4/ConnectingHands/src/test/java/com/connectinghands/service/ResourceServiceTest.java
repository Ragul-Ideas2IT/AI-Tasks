package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceDto;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRepository;
import com.connectinghands.service.impl.ResourceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private OrphanageRepository orphanageRepository;
    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Orphanage orphanage;
    private Resource resource;

    @BeforeEach
    void setUp() {
        orphanage = new Orphanage();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");

        resource = new Resource();
        resource.setId(1L);
        resource.setName("Test Resource");
        resource.setDescription("Test Description");
        resource.setCategory(ResourceCategory.FOOD);
        resource.setQuantity(10);
        resource.setUnit("kg");
        resource.setOrphanage(orphanage);
        resource.setStatus(ResourceStatus.AVAILABLE);
    }

    @Test
    void createResource_ValidRequest_ReturnsResourceDto() {
        CreateResourceRequest request = new CreateResourceRequest();
        request.setOrphanageId(1L);
        request.setName("New Resource");
        request.setDescription("New Description");
        request.setCategory(ResourceCategory.FOOD.name());
        request.setQuantity(5);
        request.setUnit("kg");

        when(orphanageRepository.findById(1L)).thenReturn(Optional.of(orphanage));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        ResourceDto dto = resourceService.createResource(request);
        assertThat(dto).isNotNull();
        assertThat(dto.getOrphanageName()).isEqualTo("Test Orphanage");
        assertThat(dto.getName()).isEqualTo("Test Resource");
        assertThat(dto.getStatus()).isEqualTo(ResourceStatus.AVAILABLE);
    }

    @Test
    void createResource_OrphanageNotFound_ThrowsException() {
        CreateResourceRequest request = new CreateResourceRequest();
        request.setOrphanageId(2L);
        when(orphanageRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceService.createResource(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }

    @Test
    void getResource_ValidId_ReturnsResourceDto() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        ResourceDto dto = resourceService.getResource(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getResource_NotFound_ThrowsException() {
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceService.getResource(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }

    @Test
    void getAllResources_ReturnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Resource> resourcePage = new PageImpl<>(Collections.singletonList(resource));
        when(resourceRepository.findAll(pageable)).thenReturn(resourcePage);
        Page<ResourceDto> page = resourceService.getAllResources(pageable);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getResourcesByOrphanage_ReturnsList() {
        when(resourceRepository.findByOrphanageId(1L)).thenReturn(Collections.singletonList(resource));
        List<ResourceDto> list = resourceService.getResourcesByOrphanage(1L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getOrphanageName()).isEqualTo("Test Orphanage");
    }

    @Test
    void getResourcesByCategory_ReturnsList() {
        when(resourceRepository.findByCategory(ResourceCategory.FOOD)).thenReturn(Collections.singletonList(resource));
        List<ResourceDto> list = resourceService.getResourcesByCategory(ResourceCategory.FOOD);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getCategory()).isEqualTo(ResourceCategory.FOOD);
    }

    @Test
    void getResourcesByStatus_ReturnsList() {
        when(resourceRepository.findByStatus(ResourceStatus.AVAILABLE)).thenReturn(Collections.singletonList(resource));
        List<ResourceDto> list = resourceService.getResourcesByStatus(ResourceStatus.AVAILABLE);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStatus()).isEqualTo(ResourceStatus.AVAILABLE);
    }

    @Test
    void updateResource_ValidRequest_ReturnsUpdatedDto() {
        UpdateResourceDto request = new UpdateResourceDto();
        request.setName("Updated Resource");
        request.setQuantity(15);
        request.setStatus(ResourceStatus.UNAVAILABLE);
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        ResourceDto dto = resourceService.updateResource(1L, request);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void updateResource_NotFound_ThrowsException() {
        UpdateResourceDto request = new UpdateResourceDto();
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceService.updateResource(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }

    @Test
    void deleteResource_ValidId_DeletesResource() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRepository).deleteById(1L);
        resourceService.deleteResource(1L);
        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResource_NotFound_ThrowsException() {
        when(resourceRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> resourceService.deleteResource(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource not found");
    }
} 