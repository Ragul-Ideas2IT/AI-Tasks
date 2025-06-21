package com.connectinghands.patterns.strategy;

public class PaymentClient {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        // Pay using Credit Card
        PaymentStrategy creditCard = new CreditCardPayment("1234-5678-9012-3456", "Test User");
        context.setPaymentStrategy(creditCard);
        System.out.println(context.executePayment(100.0));

        // Pay using PayPal
        PaymentStrategy payPal = new PayPalPayment("test.user@example.com");
        context.setPaymentStrategy(payPal);
        System.out.println(context.executePayment(250.50));

        // Pay using Bank Transfer
        PaymentStrategy bankTransfer = new BankTransferPayment("987654321");
        context.setPaymentStrategy(bankTransfer);
        System.out.println(context.executePayment(500.0));
    }
} 