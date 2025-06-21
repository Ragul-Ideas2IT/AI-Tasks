package com.connectinghands.service;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.OrphanageStatus;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.service.impl.OrphanageServiceImpl;
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
class OrphanageServiceTest {

    @Mock
    private OrphanageRepository orphanageRepository;
    @InjectMocks
    private OrphanageServiceImpl orphanageService;

    private Orphanage orphanage;

    @BeforeEach
    void setUp() {
        orphanage = new Orphanage();
        orphanage.setId(1L);
        orphanage.setName("Test Orphanage");
        orphanage.setDescription("Test Description");
        orphanage.setAddress("Test Address");
        orphanage.setPhone("1234567890");
        orphanage.setEmail("test@example.com");
        orphanage.setStatus(OrphanageStatus.ACTIVE);
    }

    @Test
    void createOrphanage_ValidRequest_ReturnsOrphanageDto() {
        CreateOrphanageRequest request = new CreateOrphanageRequest();
        request.setName("New Orphanage");
        request.setDescription("New Description");
        request.setAddress("New Address");
        request.setPhone("9876543210");
        request.setEmail("new@example.com");

        when(orphanageRepository.save(any(Orphanage.class))).thenReturn(orphanage);

        OrphanageDto dto = orphanageService.createOrphanage(request);
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Test Orphanage");
        assertThat(dto.getStatus()).isEqualTo(OrphanageStatus.ACTIVE);
    }

    @Test
    void getOrphanage_ValidId_ReturnsOrphanageDto() {
        when(orphanageRepository.findById(1L)).thenReturn(Optional.of(orphanage));
        OrphanageDto dto = orphanageService.getOrphanage(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getOrphanage_NotFound_ThrowsException() {
        when(orphanageRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orphanageService.getOrphanage(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }

    @Test
    void getAllOrphanages_ReturnsList() {
        when(orphanageRepository.findAll()).thenReturn(Collections.singletonList(orphanage));
        List<OrphanageDto> list = orphanageService.getAllOrphanages();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getOrphanagesByStatus_ReturnsList() {
        when(orphanageRepository.findByStatus(OrphanageStatus.ACTIVE))
                .thenReturn(Collections.singletonList(orphanage));
        List<OrphanageDto> list = orphanageService.getOrphanagesByStatus(OrphanageStatus.ACTIVE);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStatus()).isEqualTo(OrphanageStatus.ACTIVE);
    }

    @Test
    void updateOrphanage_ValidRequest_ReturnsUpdatedDto() {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        request.setName("Updated Orphanage");
        request.setDescription("Updated Description");
        request.setStatus(OrphanageStatus.INACTIVE);
        when(orphanageRepository.findById(1L)).thenReturn(Optional.of(orphanage));
        when(orphanageRepository.save(any(Orphanage.class))).thenReturn(orphanage);
        OrphanageDto dto = orphanageService.updateOrphanage(1L, request);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void updateOrphanage_NotFound_ThrowsException() {
        UpdateOrphanageRequest request = new UpdateOrphanageRequest();
        when(orphanageRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orphanageService.updateOrphanage(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }

    @Test
    void deleteOrphanage_ValidId_DeletesOrphanage() {
        when(orphanageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orphanageRepository).deleteById(1L);
        orphanageService.deleteOrphanage(1L);
        verify(orphanageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrphanage_NotFound_ThrowsException() {
        when(orphanageRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> orphanageService.deleteOrphanage(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Orphanage not found");
    }
} 