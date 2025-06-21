package com.connectinghands.patterns.strategy;

public class BankTransferPayment implements PaymentStrategy {

    private final String bankAccount;

    public BankTransferPayment(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String pay(double amount) {
        // Simulate bank transfer logic
        return "Paid " + amount + " using Bank Transfer from account " + bankAccount;
    }
} 