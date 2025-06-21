package com.connectinghands.patterns.observer;

public interface OrderSubject {
    void registerObserver(OrderObserver observer);
    void unregisterObserver(OrderObserver observer);
    void notifyObservers(Order order);
} 