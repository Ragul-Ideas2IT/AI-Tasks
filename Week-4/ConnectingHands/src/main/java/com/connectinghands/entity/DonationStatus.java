package com.connectinghands.entity;

/**
 * Enum representing the possible statuses of a donation.
 * PENDING: Donation is being processed
 * COMPLETED: Donation has been successfully completed
 * FAILED: Donation processing failed
 * CANCELLED: Donation was cancelled
 * REFUNDED: Donation was refunded
 *
 * @author Ragul Venkatesan
 */
public enum DonationStatus {
    /**
     * Donation is being processed.
     */
    PENDING,

    /**
     * Donation has been successfully completed.
     */
    COMPLETED,

    /**
     * Donation processing failed.
     */
    FAILED,

    /**
     * Donation was cancelled.
     */
    CANCELLED,

    /**
     * Donation was refunded.
     */
    REFUNDED
} 

