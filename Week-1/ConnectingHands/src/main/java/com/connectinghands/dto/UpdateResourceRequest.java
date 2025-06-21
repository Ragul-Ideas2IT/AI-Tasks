package com.connectinghands.dto;

import com.connectinghands.entity.ResourceRequestStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for updating an existing resource request.
 * Contains validation annotations to ensure data integrity.
 *
 * @author Ragul Venkatesan
 */
@Data
public class UpdateResourceRequest {
    /**
     * Updated name of the resource.
     */
    private String name;

    /**
     * Updated description of the resource.
     */
    private String description;

    /**
     * Updated category of the resource.
     */
    private String category;

    /**
     * Updated quantity of the resource.
     * Must be at least 1 if provided.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /**
     * Updated unit of measurement for the resource.
     */
    private String unit;

    /**
     * Updated status of the resource request.
     */
    private ResourceRequestStatus status;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStatus() { return status.toString(); }
} 