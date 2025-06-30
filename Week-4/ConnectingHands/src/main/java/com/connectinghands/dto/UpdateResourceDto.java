package com.connectinghands.dto;

import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import lombok.Data;

/**
 * Data Transfer Object for updating an existing resource.
 * Contains only updatable fields for a resource.
 *
 * @author Ragul Venkatesan
 */
@Data
public class UpdateResourceDto {
    private String name;
    private String description;
    private ResourceCategory category;
    private Integer quantity;
    private String unit;
    private ResourceStatus status;
} 