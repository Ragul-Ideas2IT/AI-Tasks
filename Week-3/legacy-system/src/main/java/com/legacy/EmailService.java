package com.legacy;

public class EmailService {
    public void sendReceipt(String email, String transactionId) {
        System.out.println("Receipt sent to " + email + " for transaction " + transactionId);
    }
} 