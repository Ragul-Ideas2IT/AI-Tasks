// AdvancedRefactorExample.java
// Demonstrates advanced refactoring on a 200+ line function
// Techniques: Extract Method, Replace Conditionals, SOLID, Parameter Objects

// ================= BEFORE REFACTOR =================
// The following is a monolithic, hard-to-maintain processOrder method.
// It handles order validation, payment, shipping, notifications, and logging in one place.

class OrderProcessor {
    // BAD: 200+ line method
    public void processOrder(Order order, User user, PaymentInfo paymentInfo, ShippingInfo shippingInfo, boolean isExpress, String couponCode, String locale) {
        // 1. Validate order
        if (order == null || user == null || paymentInfo == null || shippingInfo == null) {
            throw new IllegalArgumentException("Missing required information");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (!user.isActive()) {
            throw new IllegalStateException("User account is not active");
        }
        if (!paymentInfo.isValid()) {
            throw new IllegalArgumentException("Invalid payment info");
        }
        if (!shippingInfo.isValid()) {
            throw new IllegalArgumentException("Invalid shipping info");
        }
        // 2. Apply coupon
        double discount = 0.0;
        if (couponCode != null && !couponCode.isEmpty()) {
            if (couponCode.equals("SAVE10")) {
                discount = 0.10;
            } else if (couponCode.equals("FREESHIP")) {
                shippingInfo.setFreeShipping(true);
            } else if (couponCode.equals("VIP20") && user.isVip()) {
                discount = 0.20;
            }
        }
        // 3. Calculate total
        double subtotal = 0.0;
        for (OrderItem item : order.getItems()) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        double total = subtotal * (1 - discount);
        if (isExpress) {
            total += 15.0;
        }
        if (!shippingInfo.isFreeShipping()) {
            total += 5.0;
        }
        // 4. Process payment
        boolean paymentSuccess = false;
        if (paymentInfo.getType().equals("CREDIT_CARD")) {
            // ... (simulate 30+ lines of credit card logic)
            paymentSuccess = true;
        } else if (paymentInfo.getType().equals("PAYPAL")) {
            // ... (simulate 30+ lines of PayPal logic)
            paymentSuccess = true;
        } else if (paymentInfo.getType().equals("BANK_TRANSFER")) {
            // ... (simulate 30+ lines of bank transfer logic)
            paymentSuccess = true;
        } else {
            throw new IllegalArgumentException("Unsupported payment type");
        }
        if (!paymentSuccess) {
            throw new IllegalStateException("Payment failed");
        }
        // 5. Ship order
        if (isExpress) {
            // ... (simulate 20+ lines of express shipping logic)
        } else {
            // ... (simulate 20+ lines of standard shipping logic)
        }
        // 6. Send notifications
        if (locale.equals("en")) {
            // ... (simulate 10+ lines of English notification logic)
        } else if (locale.equals("es")) {
            // ... (simulate 10+ lines of Spanish notification logic)
        } else {
            // ... (simulate 10+ lines of default notification logic)
        }
        // 7. Log order
        System.out.println("Order processed for user: " + user.getId() + ", total: " + total);
        // ... (simulate 10+ lines of logging)
    }
}

// ================= AFTER REFACTOR =================
// The following demonstrates advanced refactoring:
// - Each responsibility is extracted to its own method/class
// - Payment and notification use strategy pattern
// - Parameter Object for order context
// - Main method is orchestration only

class OrderProcessingContext {
    public final Order order;
    public final User user;
    public final PaymentInfo paymentInfo;
    public final ShippingInfo shippingInfo;
    public final boolean isExpress;
    public final String couponCode;
    public final String locale;
    public double discount = 0.0;
    public double total = 0.0;
    public OrderProcessingContext(Order order, User user, PaymentInfo paymentInfo, ShippingInfo shippingInfo, boolean isExpress, String couponCode, String locale) {
        this.order = order;
        this.user = user;
        this.paymentInfo = paymentInfo;
        this.shippingInfo = shippingInfo;
        this.isExpress = isExpress;
        this.couponCode = couponCode;
        this.locale = locale;
    }
}

interface PaymentStrategy {
    boolean pay(OrderProcessingContext ctx);
}
class CreditCardPayment implements PaymentStrategy {
    public boolean pay(OrderProcessingContext ctx) { /* ... */ return true; }
}
class PaypalPayment implements PaymentStrategy {
    public boolean pay(OrderProcessingContext ctx) { /* ... */ return true; }
}
class BankTransferPayment implements PaymentStrategy {
    public boolean pay(OrderProcessingContext ctx) { /* ... */ return true; }
}

interface NotificationStrategy {
    void notifyUser(OrderProcessingContext ctx);
}
class EnglishNotification implements NotificationStrategy {
    public void notifyUser(OrderProcessingContext ctx) { /* ... */ }
}
class SpanishNotification implements NotificationStrategy {
    public void notifyUser(OrderProcessingContext ctx) { /* ... */ }
}
class DefaultNotification implements NotificationStrategy {
    public void notifyUser(OrderProcessingContext ctx) { /* ... */ }
}

class RefactoredOrderProcessor {
    public void processOrder(OrderProcessingContext ctx) {
        validateOrder(ctx);
        applyCoupon(ctx);
        calculateTotal(ctx);
        PaymentStrategy payment = getPaymentStrategy(ctx.paymentInfo.getType());
        if (!payment.pay(ctx)) throw new IllegalStateException("Payment failed");
        shipOrder(ctx);
        NotificationStrategy notification = getNotificationStrategy(ctx.locale);
        notification.notifyUser(ctx);
        logOrder(ctx);
    }
    private void validateOrder(OrderProcessingContext ctx) {
        if (ctx.order == null || ctx.user == null || ctx.paymentInfo == null || ctx.shippingInfo == null)
            throw new IllegalArgumentException("Missing required information");
        if (ctx.order.getItems() == null || ctx.order.getItems().isEmpty())
            throw new IllegalArgumentException("Order must have at least one item");
        if (!ctx.user.isActive())
            throw new IllegalStateException("User account is not active");
        if (!ctx.paymentInfo.isValid())
            throw new IllegalArgumentException("Invalid payment info");
        if (!ctx.shippingInfo.isValid())
            throw new IllegalArgumentException("Invalid shipping info");
    }
    private void applyCoupon(OrderProcessingContext ctx) {
        if (ctx.couponCode == null || ctx.couponCode.isEmpty()) return;
        switch (ctx.couponCode) {
            case "SAVE10": ctx.discount = 0.10; break;
            case "FREESHIP": ctx.shippingInfo.setFreeShipping(true); break;
            case "VIP20": if (ctx.user.isVip()) ctx.discount = 0.20; break;
        }
    }
    private void calculateTotal(OrderProcessingContext ctx) {
        double subtotal = 0.0;
        for (OrderItem item : ctx.order.getItems()) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        ctx.total = subtotal * (1 - ctx.discount);
        if (ctx.isExpress) ctx.total += 15.0;
        if (!ctx.shippingInfo.isFreeShipping()) ctx.total += 5.0;
    }
    private PaymentStrategy getPaymentStrategy(String type) {
        switch (type) {
            case "CREDIT_CARD": return new CreditCardPayment();
            case "PAYPAL": return new PaypalPayment();
            case "BANK_TRANSFER": return new BankTransferPayment();
            default: throw new IllegalArgumentException("Unsupported payment type");
        }
    }
    private void shipOrder(OrderProcessingContext ctx) {
        if (ctx.isExpress) {
            // ... express shipping logic
        } else {
            // ... standard shipping logic
        }
    }
    private NotificationStrategy getNotificationStrategy(String locale) {
        switch (locale) {
            case "en": return new EnglishNotification();
            case "es": return new SpanishNotification();
            default: return new DefaultNotification();
        }
    }
    private void logOrder(OrderProcessingContext ctx) {
        System.out.println("Order processed for user: " + ctx.user.getId() + ", total: " + ctx.total);
        // ... logging logic
    }
}

// ================= EXPLANATION =================
// - Extract Method: Each step is a separate method (validateOrder, applyCoupon, etc.)
// - Replace Conditionals: Payment and notification use strategy pattern
// - SOLID: Each class/method has a single responsibility
// - Parameter Object: OrderProcessingContext groups related parameters
// - Main method is orchestration only 