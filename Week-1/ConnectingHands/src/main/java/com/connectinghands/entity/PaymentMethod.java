package com.connectinghands.entity;

/**
 * Enum representing the available payment methods for donations.
 * CREDIT_CARD: Payment via credit card
 * DEBIT_CARD: Payment via debit card
 * BANK_TRANSFER: Payment via bank transfer
 * PAYPAL: Payment via PayPal
 * CASH: Cash donation
 *
 * @author Ragul Venkatesan
 */
public enum PaymentMethod {
    /**
     * Payment via credit card.
     */
    CREDIT_CARD,

    /**
     * Payment via debit card.
     */
    DEBIT_CARD,

    /**
     * Payment via bank transfer.
     */
    BANK_TRANSFER,

    /**
     * Payment via PayPal.
     */
    PAYPAL,

    /**
     * Cash donation.
     */
    CASH
} 