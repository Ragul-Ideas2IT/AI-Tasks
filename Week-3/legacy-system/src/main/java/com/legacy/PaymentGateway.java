package com.legacy;

public class PaymentGateway {
    public boolean validateCard(String cardToken) {
        return cardToken != null && !cardToken.isEmpty();
    }

    public String charge(String cardToken, double total) {
        if (!validateCard(cardToken)) {
            return null;
        }
        // Simulate a successful charge
        return "tx_12345";
    }
} 