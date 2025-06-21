package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequestRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequestRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRequestRepository;
import com.connectinghands.service.impl.ResourceRequestServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceRequestServiceTest {

    @Mock
    private ResourceRequestRepository resourceRequestRepository;
    @Mock
    private OrphanageRepository orphanageRepository;
    @InjectMocks
    private ResourceRequestServiceImpl resourceRequestService;

    private Orphanage orphanage;
    private ResourceRequest resourceRequest;

    @BeforeEach
    void setUp() {
        orphanage = new Orphanage();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");

        resourceRequest = new ResourceRequest();
        resourceRequest.setId(1L);
        resourceRequest.setName("Test Request");
        resourceRequest.setDescription("Test Description");
        resourceRequest.setCategory(ResourceCategory.FOOD);
        resourceRequest.setQuantity(10);
        resourceRequest.setUnit("kg");
        resourceRequest.setOrphanage(orphanage);
        resourceRequest.setStatus(ResourceRequestStatus.PENDING);
    }

    @Test
    void createResourceRequest_ValidRequest_ReturnsResourceRequestDto() {
        CreateResourceRequestRequest request = new CreateResourceRequestRequest();
        request.setOrphanageId(1L);
        request.setName("New Request");
        request.setDescription("New Description");
        request.setCategory(ResourceCategory.FOOD);
        request.setQuantity(5);
        request.setUnit("kg");

        when(orphanageRepository.findById(1L)).thenReturn(Optional.of(orphanage));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenReturn(resourceRequest);

        ResourceRequestDto dto = resourceRequestService.createResourceRequest(request);
        assertThat(dto).isNotNull();
        assertThat(dto.getOrphanageId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Request");
        assertThat(dto.getStatus()).isEqualTo(ResourceRequestStatus.PENDING);
    }

    @Test
    void createResourceRequest_OrphanageNotFound_ThrowsException() {
        CreateResourceRequestRequest request = new CreateResourceRequestRequest();
        request.setOrphanageId(2L);
        when(orphanageRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceRequestService.createResourceRequest(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }

    @Test
    void getResourceRequest_ValidId_ReturnsResourceRequestDto() {
        when(resourceRequestRepository.findById(1L)).thenReturn(Optional.of(resourceRequest));
        ResourceRequestDto dto = resourceRequestService.getResourceRequest(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getResourceRequest_NotFound_ThrowsException() {
        when(resourceRequestRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceRequestService.getResourceRequest(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource request not found");
    }

    @Test
    void getAllResourceRequests_ReturnsList() {
        when(resourceRequestRepository.findAll()).thenReturn(Collections.singletonList(resourceRequest));
        List<ResourceRequestDto> list = resourceRequestService.getAllResourceRequests();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getResourceRequestsByOrphanage_ReturnsList() {
        when(resourceRequestRepository.findByOrphanageId(1L)).thenReturn(Collections.singletonList(resourceRequest));
        List<ResourceRequestDto> list = resourceRequestService.getResourceRequestsByOrphanage(1L);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getOrphanageId()).isEqualTo(1L);
    }

    @Test
    void getResourceRequestsByStatus_ReturnsList() {
        when(resourceRequestRepository.findByStatus(ResourceRequestStatus.PENDING))
                .thenReturn(Collections.singletonList(resourceRequest));
        List<ResourceRequestDto> list = resourceRequestService.getResourceRequestsByStatus(ResourceRequestStatus.PENDING);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStatus()).isEqualTo(ResourceRequestStatus.PENDING);
    }

    @Test
    void updateResourceRequest_ValidRequest_ReturnsUpdatedDto() {
        UpdateResourceRequestRequest request = new UpdateResourceRequestRequest();
        request.setName("Updated Request");
        request.setQuantity(15);
        request.setStatus(ResourceRequestStatus.FULFILLED);
        when(resourceRequestRepository.findById(1L)).thenReturn(Optional.of(resourceRequest));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenReturn(resourceRequest);
        ResourceRequestDto dto = resourceRequestService.updateResourceRequest(1L, request);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void updateResourceRequest_NotFound_ThrowsException() {
        UpdateResourceRequestRequest request = new UpdateResourceRequestRequest();
        when(resourceRequestRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> resourceRequestService.updateResourceRequest(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource request not found");
    }

    @Test
    void deleteResourceRequest_ValidId_DeletesResourceRequest() {
        when(resourceRequestRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRequestRepository).deleteById(1L);
        resourceRequestService.deleteResourceRequest(1L);
        verify(resourceRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResourceRequest_NotFound_ThrowsException() {
        when(resourceRequestRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> resourceRequestService.deleteResourceRequest(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Resource request not found");
    }
} 