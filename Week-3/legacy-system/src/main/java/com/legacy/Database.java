package com.legacy;

public class Database {
    public User getUser(int userId) {
        // In a real system, this would query a database.
        // Returning a dummy user for now.
        if (userId == 1) {
            return new User(1, "test@example.com", 1000, "premium");
        }
        return null;
    }

    public void updateUser(User user) {
        // In a real system, this would update the user in the database.
        System.out.println("User updated: " + user.getId());
    }
} 