package com.connectinghands.patterns.observer;

public class InventoryService implements OrderObserver {
    @Override
    public void update(Order order) {
        // Simulate updating the inventory
        System.out.println("InventoryService: Decreasing stock for product " + order.getProductName() + " by " + order.getQuantity());
    }
} 