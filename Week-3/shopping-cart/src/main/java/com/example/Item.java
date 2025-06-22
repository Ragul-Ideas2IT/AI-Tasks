package com.example;

/**
 * Represents an item in the shopping cart.
 */
public class Item {
    private String name;
    private int quantity;
    private double price;

    /**
     * Constructs a new Item.
     *
     * @param name     the name of the item
     * @param quantity the quantity of the item
     * @param price    the price of the item
     */
    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the quantity of the item.
     *
     * @return the quantity of the item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the price of the item.
     *
     * @return the price of the item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 