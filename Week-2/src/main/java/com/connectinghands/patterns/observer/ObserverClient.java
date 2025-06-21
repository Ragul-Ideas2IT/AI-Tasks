package com.connectinghands.patterns.observer;

public class ObserverClient {
    public static void main(String[] args) {
        // Create the subject
        OrderService orderService = new OrderService();

        // Create observers
        OrderObserver emailService = new EmailService();
        OrderObserver inventoryService = new InventoryService();

        // Register observers with the subject
        orderService.registerObserver(emailService);
        orderService.registerObserver(inventoryService);

        // Place a new order, which will trigger notifications
        System.out.println("Placing a new order...");
        Order order1 = new Order("ORDER-123", "Laptop", 1);
        orderService.placeOrder(order1);

        System.out.println("\n-----------------------------------\n");

        // Unregister an observer
        System.out.println("Unregistering the inventory service...");
        orderService.unregisterObserver(inventoryService);

        // Place another order
        System.out.println("\nPlacing another order...");
        Order order2 = new Order("ORDER-456", "Mouse", 2);
        orderService.placeOrder(order2);
    }
} 