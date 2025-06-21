package com.connectinghands.dto;

import com.connectinghands.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new donation.
 * Contains validation annotations to ensure data integrity.
 *
 * @author Ragul Venkatesan
 */
@Data
public class CreateDonationRequest {
    /**
     * ID of the orphanage receiving the donation.
     * Must not be null.
     */
    @NotNull(message = "Orphanage ID is required")
    private Long orphanageId;

    /**
     * Monetary amount of the donation.
     * Must be positive if provided.
     */
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * Currency of the monetary donation.
     * Must be 3 characters if provided.
     */
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    /**
     * Method used for the payment.
     * Required for monetary donations.
     */
    private PaymentMethod paymentMethod;

    /**
     * Additional notes about the donation.
     * Maximum 1000 characters.
     */
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
} 