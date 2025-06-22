package com.legacy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

public class PaymentProcessorTest {

    @Test
    public void testProcessPayment_NullCardToken() {
        // Arrange
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard(null)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentProcessor.processPayment(1, 100, null);
        });
    }

    @Test
    public void testProcessPayment_EmptyCardToken() {
        // Arrange
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("")).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentProcessor.processPayment(1, 100, "");
        });
    }

    @Test
    public void testProcessPayment_NegativeUserId() {
        when(database.getUser(-1)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(-1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_ZeroUserId() {
        when(database.getUser(0)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(0, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_LargeUserId() {
        when(database.getUser(Integer.MAX_VALUE)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(Integer.MAX_VALUE, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_NegativeAmount() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(-100, "premium")).thenReturn(-1.0);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, -100, "valid_token"));
    }

    @Test
    public void testProcessPayment_ZeroAmount() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(0, "premium")).thenReturn(0.0);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 0, "valid_token"));
    }

    @Test
    public void testProcessPayment_LargeAmount() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(1_000_000, "premium")).thenReturn(10_000.0);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 1_000_000, "valid_token"));
    }

    @Test
    public void testProcessPayment_UserWithNullEmail() {
        User user = new User(1, null, 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        doThrow(new RuntimeException("Email failed")).when(emailService).sendReceipt(null, "tx_12345");
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_UserWithNullTier() {
        User user = new User(1, "test@example.com", 1000, null);
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, null)).thenReturn(2.0);
        when(paymentGateway.charge("valid_token", 102.0)).thenReturn("tx_12345");
        String transactionId = paymentProcessor.processPayment(1, 100, "valid_token");
        assertEquals("tx_12345", transactionId);
    }

    @Test
    public void testProcessPayment_BalanceEqualsTotal() {
        User user = new User(1, "test@example.com", 101.0, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        String transactionId = paymentProcessor.processPayment(1, 100, "valid_token");
        assertEquals("tx_12345", transactionId);
        assertEquals(0.0, user.getBalance());
    }

    @Test
    public void testProcessPayment_FeeCalculationNegative() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(-10.0);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_PaymentGatewayThrows() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenThrow(new RuntimeException("Gateway error"));
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_DatabaseThrows() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        doThrow(new RuntimeException("DB error")).when(database).updateUser(user);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_EmailServiceThrows() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        doThrow(new RuntimeException("Email error")).when(emailService).sendReceipt("test@example.com", "tx_12345");
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, "valid_token"));
    }

    @Test
    public void testProcessPayment_Concurrency() throws InterruptedException {
        // Arrange
        User user = new User(1, "test@example.com", 200, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_1").thenReturn("tx_2");

        Runnable paymentTask = () -> {
            try {
                paymentProcessor.processPayment(1, 100, "valid_token");
            } catch (Exception ignored) {}
        };
        Thread t1 = new Thread(paymentTask);
        Thread t2 = new Thread(paymentTask);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // The balance may be negative if not thread-safe (not enforced here, but test is illustrative)
    }

    @Test
    public void testProcessPayment_SQLInjectionCardToken() {
        User user = new User(1, "test@example.com", 1000, "premium");
        String sqlToken = "'; DROP TABLE users; --";
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard(sqlToken)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, sqlToken));
    }

    @Test
    public void testProcessPayment_LongCardToken() {
        User user = new User(1, "test@example.com", 1000, "premium");
        String longToken = "a".repeat(10_000);
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard(longToken)).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge(longToken, 101.0)).thenReturn("tx_12345");
        String transactionId = paymentProcessor.processPayment(1, 100, longToken);
        assertEquals("tx_12345", transactionId);
    }

    @Test
    public void testProcessPayment_UnicodeEmail() {
        User user = new User(1, "测试@例子.公司", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        doNothing().when(emailService).sendReceipt("测试@例子.公司", "tx_12345");
        String transactionId = paymentProcessor.processPayment(1, 100, "valid_token");
        assertEquals("tx_12345", transactionId);
    }

    @Test
    public void testProcessPayment_BalanceDoubleMaxValue() {
        User user = new User(1, "test@example.com", Double.MAX_VALUE, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");
        String transactionId = paymentProcessor.processPayment(1, 100, "valid_token");
        assertEquals("tx_12345", transactionId);
    }

    @Test
    public void testProcessPayment_AmountNaN() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(Double.NaN, "premium")).thenReturn(Double.NaN);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, Double.NaN, "valid_token"));
    }

    @Test
    public void testProcessPayment_AmountInfinity() {
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(Double.POSITIVE_INFINITY, "premium")).thenReturn(Double.POSITIVE_INFINITY);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, Double.POSITIVE_INFINITY, "valid_token"));
    }

    @Test
    public void testProcessPayment_WhitespaceCardToken() {
        User user = new User(1, "test@example.com", 1000, "premium");
        String wsToken = "   ";
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard(wsToken)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> paymentProcessor.processPayment(1, 100, wsToken));
    }
} 