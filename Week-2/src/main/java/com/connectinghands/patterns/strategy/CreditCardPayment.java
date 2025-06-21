package com.connectinghands.patterns.strategy;

public class CreditCardPayment implements PaymentStrategy {

    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public String pay(double amount) {
        // Simulate credit card payment logic
        return "Paid " + amount + " using Credit Card ending in " + cardNumber.substring(cardNumber.length() - 4);
    }
} 