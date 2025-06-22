package com.legacy;

public class FeeCalculator {
    public double calculateFee(double amount, String tier) {
        if ("premium".equals(tier)) {
            return amount * 0.01; // 1% fee for premium users
        }
        return amount * 0.02; // 2% fee for standard users
    }
} 