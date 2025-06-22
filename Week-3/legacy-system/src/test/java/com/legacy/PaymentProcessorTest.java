package com.legacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorTest {

    @Mock
    private Database database;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private EmailService emailService;

    @Mock
    private FeeCalculator feeCalculator;

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    @Test
    public void testProcessPayment_Success() {
        // Arrange
        User user = new User(1, "test@example.com", 1000, "premium");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "premium")).thenReturn(1.0);
        when(paymentGateway.charge("valid_token", 101.0)).thenReturn("tx_12345");

        // Act
        String transactionId = paymentProcessor.processPayment(1, 100, "valid_token");

        // Assert
        assertEquals("tx_12345", transactionId);
        verify(database).updateUser(user);
        assertEquals(899.0, user.getBalance());
        verify(emailService).sendReceipt("test@example.com", "tx_12345");
    }

    @Test
    public void testProcessPayment_UserNotFound() {
        // Arrange
        when(database.getUser(2)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentProcessor.processPayment(2, 100, "valid_token");
        });
    }

    @Test
    public void testProcessPayment_InsufficientFunds() {
        // Arrange
        User user = new User(1, "test@example.com", 50, "standard");
        when(database.getUser(1)).thenReturn(user);
        when(paymentGateway.validateCard("valid_token")).thenReturn(true);
        when(feeCalculator.calculateFee(100, "standard")).thenReturn(2.0);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentProcessor.processPayment(1, 100, "valid_token");
        });
    }
} 