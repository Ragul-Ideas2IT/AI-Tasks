package com.connectinghands.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements OrderSubject {

    private final List<OrderObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.update(order);
        }
    }

    public void placeOrder(Order order) {
        System.out.println("Processing new order: " + order.getOrderId());
        // ... business logic for placing an order ...
        System.out.println("Order processed. Notifying observers...");
        notifyObservers(order);
    }
} 