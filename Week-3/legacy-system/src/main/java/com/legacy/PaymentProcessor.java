package com.legacy;

public class PaymentProcessor {
    private final Database database;
    private final PaymentGateway paymentGateway;
    private final EmailService emailService;
    private final FeeCalculator feeCalculator;

    public PaymentProcessor() {
        this.database = new Database();
        this.paymentGateway = new PaymentGateway();
        this.emailService = new EmailService();
        this.feeCalculator = new FeeCalculator();
    }

    public PaymentProcessor(Database database, PaymentGateway paymentGateway, EmailService emailService, FeeCalculator feeCalculator) {
        this.database = database;
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
        this.feeCalculator = feeCalculator;
    }

    /**
     * This is the original, untestable method. It remains for backward compatibility.
     */
    public String processPayment(int userId, double amount, String cardToken) {
        User user = database.getUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!paymentGateway.validateCard(cardToken)) {
            throw new RuntimeException("Invalid card");
        }

        double fee = feeCalculator.calculateFee(amount, user.getTier());
        double total = amount + fee;

        if (user.getBalance() < total) {
            throw new RuntimeException("Insufficient funds");
        }

        String transactionId = paymentGateway.charge(cardToken, total);
        user.setBalance(user.getBalance() - total);
        database.updateUser(user);
        emailService.sendReceipt(user.getEmail(), transactionId);

        return transactionId;
    }

    /**
     * This is the new, testable method with dependencies injected.
     */
    public String processPayment(int userId, double amount, String cardToken,
                                 Database database, PaymentGateway paymentGateway,
                                 EmailService emailService, FeeCalculator feeCalculator) {
        User user = database.getUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!paymentGateway.validateCard(cardToken)) {
            throw new RuntimeException("Invalid card");
        }

        double fee = feeCalculator.calculateFee(amount, user.getTier());
        double total = amount + fee;

        if (user.getBalance() < total) {
            throw new RuntimeException("Insufficient funds");
        }

        String transactionId = paymentGateway.charge(cardToken, total);
        user.setBalance(user.getBalance() - total);
        database.updateUser(user);
        emailService.sendReceipt(user.getEmail(), transactionId);

        return transactionId;
    }
} 