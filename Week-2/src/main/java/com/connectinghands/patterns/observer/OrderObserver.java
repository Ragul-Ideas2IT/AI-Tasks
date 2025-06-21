package com.connectinghands.patterns.observer;

// A simple class to represent an order
class Order {
    private final String orderId;
    private final String productName;
    private final int quantity;

    public Order(String orderId, String productName, int quantity) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
}

// The Observer interface
public interface OrderObserver {
    void update(Order order);
} 