package com.connectinghands.dto;

import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning resource request data.
 * Used to transfer resource request information between layers.
 *
 * @author Ragul Venkatesan
 */
@Data
public class ResourceRequestDto {
    private Long id;
    private String name;
    private String description;
    private ResourceCategory category;
    private Integer quantity;
    private String unit;
    private String orphanageName;
    private ResourceRequestStatus status;
    private Long fulfilledBy;
    private LocalDateTime fulfilledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ResourceCategory getCategory() { return category; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getOrphanageName() { return orphanageName; }
    public ResourceRequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(ResourceCategory category) { this.category = category; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setOrphanageName(String orphanageName) { this.orphanageName = orphanageName; }
    public void setStatus(ResourceRequestStatus status) { this.status = status; }
    public void setFulfilledBy(Long fulfilledBy) { this.fulfilledBy = fulfilledBy; }
    public void setFulfilledAt(java.time.LocalDateTime fulfilledAt) { this.fulfilledAt = fulfilledAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 