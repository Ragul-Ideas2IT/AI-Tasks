package com.connectinghands.dto;

import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Resource entities.
 * Used to transfer resource data between layers of the application.
 *
 * @author Ragul Venkatesan
 */
public class ResourceDto {
    /**
     * Unique identifier of the resource.
     */
    private Long id;

    /**
     * Name of the resource.
     */
    private String name;

    /**
     * Detailed description of the resource.
     */
    private String description;

    /**
     * Category of the resource.
     */
    private ResourceCategory category;

    /**
     * Current quantity of the resource.
     */
    private Integer quantity;

    /**
     * Unit of measurement for the resource.
     */
    private String unit;

    /**
     * ID of the orphanage that owns this resource.
     */
    private String orphanageName;

    /**
     * Current status of the resource.
     */
    private ResourceStatus status;

    /**
     * Creation date of the resource.
     */
    private LocalDateTime createdAt;

    /**
     * Last update date of the resource.
     */
    private LocalDateTime updatedAt;

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ResourceCategory getCategory() { return category; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getOrphanageName() { return orphanageName; }
    public ResourceStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(ResourceCategory category) { this.category = category; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setOrphanageName(String orphanageName) { this.orphanageName = orphanageName; }
    public void setStatus(ResourceStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 