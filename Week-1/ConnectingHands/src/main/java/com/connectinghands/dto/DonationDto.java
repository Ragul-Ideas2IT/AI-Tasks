package com.connectinghands.dto;

import com.connectinghands.entity.DonationStatus;
import com.connectinghands.entity.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Donation entities.
 * Used to transfer donation data between layers of the application.
 *
 * @author Ragul Venkatesan
 */
@Data
public class DonationDto {
    /**
     * Unique identifier of the donation.
     */
    private Long id;

    /**
     * ID of the donor who made the donation.
     */
    private Long donorId;

    /**
     * ID of the orphanage receiving the donation.
     */
    private Long orphanageId;

    /**
     * Monetary amount of the donation.
     * Null for in-kind donations.
     */
    private BigDecimal amount;

    /**
     * Currency of the monetary donation.
     * Null for in-kind donations.
     */
    private String currency;

    /**
     * Current status of the donation.
     */
    private DonationStatus status;

    /**
     * Method used for the payment.
     * Null for in-kind donations.
     */
    private PaymentMethod paymentMethod;

    /**
     * Unique identifier for the payment transaction.
     * Null for in-kind donations.
     */
    private String transactionId;

    /**
     * Additional notes about the donation.
     */
    private String notes;

    /**
     * Timestamp when the donation was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the donation was last updated.
     */
    private LocalDateTime updatedAt;
} 