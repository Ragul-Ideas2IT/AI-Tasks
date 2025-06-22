package com.legacy;

public class User {
    private int id;
    private String email;
    private double balance;
    private String tier;

    public User(int id, String email, double balance, String tier) {
        this.id = id;
        this.email = email;
        this.balance = balance;
        this.tier = tier;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public String getTier() { return tier; }
    public void setBalance(double balance) { this.balance = balance; }
} 