package com.legacy;

/**
 * Handles payment processing for users.
 * <p>
 * Not thread-safe. For concurrent use, synchronize externally.
 */
public class PaymentProcessor {
    private final Database database;
    private final PaymentGateway paymentGateway;
    private final EmailService emailService;
    private final FeeCalculator feeCalculator;

    /**
     * Constructs a PaymentProcessor with default dependencies.
     */
    public PaymentProcessor() {
        this.database = new Database();
        this.paymentGateway = new PaymentGateway();
        this.emailService = new EmailService();
        this.feeCalculator = new FeeCalculator();
    }

    /**
     * Constructs a PaymentProcessor with injected dependencies (for testing).
     */
    public PaymentProcessor(Database database, PaymentGateway paymentGateway, EmailService emailService, FeeCalculator feeCalculator) {
        this.database = database;
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
        this.feeCalculator = feeCalculator;
    }

    /**
     * Processes a payment for a user.
     *
     * @param userId    the user ID
     * @param amount    the amount to charge (must be positive, finite, not NaN)
     * @param cardToken the card token (must be non-null, non-empty)
     * @return the transaction ID
     * @throws RuntimeException if any validation or processing step fails
     */
    public String processPayment(int userId, double amount, String cardToken) {
        if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new RuntimeException("Amount must be positive, finite, and not NaN");
        }
        if (cardToken == null || cardToken.trim().isEmpty()) {
            throw new RuntimeException("Card token must be non-null and non-empty");
        }
        User user = database.getUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!paymentGateway.validateCard(cardToken)) {
            throw new RuntimeException("Invalid card");
        }
        double fee = feeCalculator.calculateFee(amount, user.getTier());
        if (fee < 0 || Double.isNaN(fee) || Double.isInfinite(fee)) {
            throw new RuntimeException("Fee must be non-negative, finite, and not NaN");
        }
        double total = amount + fee;
        if (user.getBalance() < total) {
            throw new RuntimeException("Insufficient funds");
        }
        String transactionId;
        try {
            transactionId = paymentGateway.charge(cardToken, total);
        } catch (Exception e) {
            throw new RuntimeException("Payment gateway error: " + e.getMessage(), e);
        }
        user.setBalance(user.getBalance() - total);
        try {
            database.updateUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Database update error: " + e.getMessage(), e);
        }
        try {
            emailService.sendReceipt(user.getEmail(), transactionId);
        } catch (Exception e) {
            throw new RuntimeException("Email service error: " + e.getMessage(), e);
        }
        return transactionId;
    }
} 