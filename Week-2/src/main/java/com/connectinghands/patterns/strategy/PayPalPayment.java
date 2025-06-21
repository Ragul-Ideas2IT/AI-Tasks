package com.connectinghands.patterns.strategy;

public class PayPalPayment implements PaymentStrategy {

    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public String pay(double amount) {
        // Simulate PayPal payment logic
        return "Paid " + amount + " using PayPal account " + email;
    }
} 