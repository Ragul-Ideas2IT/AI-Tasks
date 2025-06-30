package com.connectinghands.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;

import java.time.LocalDateTime;

/**
 * Entity representing a resource request made by an orphanage.
 * Tracks the status and details of resource requests.
 *
 * @author Ragul Venkatesan
 */
@Data   
@Entity
@Table(name = "resource_requests")
@EntityListeners(AuditingEntityListener.class)
public class ResourceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank(message = "Unit is required")
    @Column(nullable = false)
    private String unit;

    @NotNull(message = "Orphanage is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orphanage_id", nullable = false)
    private Orphanage orphanage;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceRequestStatus status = ResourceRequestStatus.PENDING;

    @Column(name = "fulfilled_by")
    private Long fulfilledBy;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Orphanage getOrphanage() { return orphanage; }
    public void setOrphanage(Orphanage orphanage) { this.orphanage = orphanage; }
    public ResourceRequestStatus getStatus() { return status; }
    public void setStatus(ResourceRequestStatus status) { this.status = status; }
    public Long getFulfilledBy() { return fulfilledBy; }
    public void setFulfilledBy(Long fulfilledBy) { this.fulfilledBy = fulfilledBy; }
    public java.time.LocalDateTime getFulfilledAt() { return fulfilledAt; }
    public void setFulfilledAt(java.time.LocalDateTime fulfilledAt) { this.fulfilledAt = fulfilledAt; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
