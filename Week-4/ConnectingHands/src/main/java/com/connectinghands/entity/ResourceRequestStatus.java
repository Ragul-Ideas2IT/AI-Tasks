package com.connectinghands.entity;

/**
 * Enum representing the possible states of a resource request.
 * PENDING: Request has been created but not yet fulfilled
 * FULFILLED: Request has been fulfilled by a donor
 * CANCELLED: Request has been cancelled by the orphanage
 * EXPIRED: Request has expired without being fulfilled
 *
 * @author Ragul Venkatesan
 */
public enum ResourceRequestStatus {
    PENDING,
    FULFILLED,
    CANCELLED,
    EXPIRED
} 

