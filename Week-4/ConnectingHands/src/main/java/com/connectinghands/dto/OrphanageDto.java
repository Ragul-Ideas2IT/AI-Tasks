package com.connectinghands.dto;

import com.connectinghands.entity.OrphanageStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Orphanage entity.
 * 
 * @author Ragul Venkatesan
 */
@Data
public class OrphanageDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String email;
    private String website;
    private Integer capacity;
    private Integer currentChildren;
    private OrphanageStatus status;
    private Long adminId;
    private String adminName;
    private String verificationDocuments;
    private String verificationNotes;
    private LocalDateTime verifiedAt;
    private Long verifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
