package com.connectinghands.patterns.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrategyPatternTest {

    private PaymentContext context;

    @BeforeEach
    void setUp() {
        context = new PaymentContext();
    }

    @Test
    void testCreditCardPayment() {
        PaymentStrategy creditCard = new CreditCardPayment("1111-2222-3333-4444", "Test User");
        context.setPaymentStrategy(creditCard);
        String result = context.executePayment(150.75);
        assertEquals("Paid 150.75 using Credit Card ending in 4444", result);
    }

    @Test
    void testPayPalPayment() {
        PaymentStrategy payPal = new PayPalPayment("test@example.com");
        context.setPaymentStrategy(payPal);
        String result = context.executePayment(200.00);
        assertEquals("Paid 200.0 using PayPal account test@example.com", result);
    }

    @Test
    void testBankTransferPayment() {
        PaymentStrategy bankTransfer = new BankTransferPayment("123456789");
        context.setPaymentStrategy(bankTransfer);
        String result = context.executePayment(500.25);
        assertEquals("Paid 500.25 using Bank Transfer from account 123456789", result);
    }

    @Test
    void testThrowsExceptionWhenStrategyNotSet() {
        assertThrows(IllegalStateException.class, () -> {
            context.executePayment(100.0);
        });
    }

    @Test
    void testDynamicStrategyChange() {
        // First, pay with PayPal
        PaymentStrategy payPal = new PayPalPayment("first@example.com");
        context.setPaymentStrategy(payPal);
        assertEquals("Paid 50.0 using PayPal account first@example.com", context.executePayment(50.0));

        // Then, switch to Credit Card
        PaymentStrategy creditCard = new CreditCardPayment("9876-5432-1098-7654", "Another User");
        context.setPaymentStrategy(creditCard);
        assertEquals("Paid 120.0 using Credit Card ending in 7654", context.executePayment(120.0));
    }
} 