package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart that holds items.
 */
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();

    /**
     * Adds an item to the cart. If the item already exists, it increases its quantity.
     *
     * @param name     the name of the item
     * @param quantity the quantity to add
     * @param price    the price of the item
     */
    public void addItem(String name, int quantity, double price) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new Item(name, quantity, price));
    }

    /**
     * Returns the list of items in the cart.
     *
     * @return the list of items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Calculates the total price of all items in the cart.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
    }

    /**
     * Removes an item from the cart by its name.
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        items.removeIf(item -> item.getName().equals(name));
    }
} 