package com.connectinghands.patterns.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ObserverPatternTest {

    @Mock
    private EmailService mockEmailService;

    @Mock
    private InventoryService mockInventoryService;

    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        order = new Order("ORDER-123", "Test Product", 1);
    }

    @Test
    void testRegisterAndNotifyObservers() {
        // Register observers
        orderService.registerObserver(mockEmailService);
        orderService.registerObserver(mockInventoryService);

        // Place an order to trigger notification
        orderService.placeOrder(order);

        // Verify that the update method was called on both observers
        verify(mockEmailService, times(1)).update(order);
        verify(mockInventoryService, times(1)).update(order);
    }

    @Test
    void testUnregisterObserver() {
        // Register observers
        orderService.registerObserver(mockEmailService);
        orderService.registerObserver(mockInventoryService);

        // Unregister one observer
        orderService.unregisterObserver(mockInventoryService);

        // Place an order
        orderService.placeOrder(order);

        // Verify that the update method was called only on the remaining observer
        verify(mockEmailService, times(1)).update(order);
        verify(mockInventoryService, times(0)).update(order);
    }

    @Test
    void testNotifyNoObservers() {
        // Place an order without any registered observers
        orderService.placeOrder(order);

        // Verify that no update methods were called (as there are no mocks to verify)
        // This test mainly ensures no NullPointerException or other errors occur.
        // To be more explicit, you could add a mock and verify it's not called.
        verify(mockEmailService, times(0)).update(order);
    }
} 