package com.connectinghands.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity class representing a resource in the system.
 * Resources are items that can be donated or requested by orphanages.
 *
 * @author Ragul Venkatesan
 */
@Data
@Entity
@Table(name = "resources")
@EntityListeners(AuditingEntityListener.class)
public class Resource {
    /**
     * Unique identifier for the resource.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the resource.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Detailed description of the resource.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Category of the resource (e.g., Food, Clothing, Education).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory category;

    /**
     * Current quantity of the resource available.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Unit of measurement for the resource (e.g., pieces, kg, liters).
     */
    @Column(nullable = false)
    private String unit;

    /**
     * The orphanage that owns or manages this resource.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orphanage_id", nullable = false)
    private Orphanage orphanage;

    /**
     * Current status of the resource.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    /**
     * Timestamp when the resource was created.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the resource was last updated.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ResourceCategory getCategory() { return category; }
    public void setCategory(ResourceCategory category) { this.category = category; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Orphanage getOrphanage() { return orphanage; }
    public void setOrphanage(Orphanage orphanage) { this.orphanage = orphanage; }
    public ResourceStatus getStatus() { return status; }
    public void setStatus(ResourceStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 

