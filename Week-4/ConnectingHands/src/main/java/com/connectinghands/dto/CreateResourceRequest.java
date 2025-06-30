package com.connectinghands.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for creating a new resource request.
 * Contains validation annotations to ensure data integrity.
 *
 * @author Ragul Venkatesan
 */
@Data
public class CreateResourceRequest {
    /**
     * Name of the resource to be created.
     * Must not be blank.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Detailed description of the resource.
     */
    private String description;

    /**
     * Category of the resource.
     * Must not be blank.
     */
    @NotBlank(message = "Category is required")
    private String category;

    /**
     * Initial quantity of the resource.
     * Must be at least 1.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /**
     * Unit of measurement for the resource.
     * Must not be blank.
     */
    @NotBlank(message = "Unit is required")
    private String unit;

    /**
     * ID of the orphanage that will own this resource.
     * Must not be null.
     */
    @NotNull(message = "Orphanage ID is required")
    private Long orphanageId;

    public Long getOrphanageId() { return orphanageId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}
