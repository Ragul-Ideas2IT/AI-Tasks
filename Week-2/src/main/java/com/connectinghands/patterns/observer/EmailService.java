package com.connectinghands.patterns.observer;

public class EmailService implements OrderObserver {
    @Override
    public void update(Order order) {
        // Simulate sending an email notification
        System.out.println("EmailService: Sending confirmation email for order " + order.getOrderId());
    }
} 