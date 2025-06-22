package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.Arrays;


public class ShoppingCartTest {
    @Test
    public void testAddItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("apple", 1, 10.0);
        assertTrue(cart.getItems().stream().anyMatch(item -> item.getName().equals("apple")));
    }

    @Test
    public void testAddExistingItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("apple", 1, 10.0);
        cart.addItem("apple", 2, 10.0);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    public void testGetTotalPrice() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("apple", 2, 1.50);
        cart.addItem("banana", 3, 0.75);
        assertEquals(5.25, cart.getTotalPrice());
    }

    @Test
    public void testRemoveItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("apple", 3, 1.50);
        cart.addItem("banana", 2, 0.75);
        cart.removeItem("apple");
        assertFalse(cart.getItems().stream().anyMatch(item -> item.getName().equals("apple")));
        assertEquals(1, cart.getItems().size());
    }
} 