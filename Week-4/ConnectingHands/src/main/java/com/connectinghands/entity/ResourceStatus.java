package com.connectinghands.entity;

/**
 * Enum representing the possible statuses of a resource.
 * AVAILABLE: Resource is available for use or donation
 * UNAVAILABLE: Resource is not available (out of stock or reserved)
 * PENDING: Resource status is being reviewed or processed
 *
 * @author Ragul Venkatesan
 */
public enum ResourceStatus {
    /**
     * Resource is available for use or donation.
     */
    AVAILABLE,

    /**
     * Resource is not available (out of stock or reserved).
     */
    UNAVAILABLE,

    /**
     * Resource status is being reviewed or processed.
     */
    PENDING
} 

