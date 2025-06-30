package com.connectinghands.entity;

/**
 * Enum representing the different states of an orphanage in the system.
 * 
 * @author Ragul Venkatesan
 */
public enum OrphanageStatus {
    /**
     * Initial state when an orphanage is registered but not yet verified
     */
    PENDING,

    /**
     * State when an orphanage is verified and active
     */
    ACTIVE,

    /**
     * State when an orphanage is temporarily suspended
     */
    SUSPENDED,

    /**
     * State when an orphanage is rejected during verification
     */
    REJECTED,

    /**
     * State when an orphanage is permanently closed
     */
    CLOSED
} 

