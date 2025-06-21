package com.connectinghands.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a donation in the system.
 * Donations can be monetary or in-kind (resources).
 *
 * @author Ragul Venkatesan
 */
@Data
@Entity
@Table(name = "donations")
@EntityListeners(AuditingEntityListener.class)
public class Donation {
    /**
     * Unique identifier for the donation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The donor who made the donation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    /**
     * The orphanage receiving the donation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orphanage_id", nullable = false)
    private Orphanage orphanage;

    /**
     * The monetary amount of the donation.
     * Null for in-kind donations.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The currency of the monetary donation.
     * Null for in-kind donations.
     */
    @Column(length = 3)
    private String currency;

    /**
     * Current status of the donation.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    /**
     * Method used for the payment.
     * Null for in-kind donations.
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * Unique identifier for the payment transaction.
     * Null for in-kind donations.
     */
    @Column(unique = true)
    private String transactionId;

    /**
     * Additional notes about the donation.
     */
    @Column(length = 1000)
    private String notes;

    /**
     * Timestamp when the donation was created.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the donation was last updated.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
} 