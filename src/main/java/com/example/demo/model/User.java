package com.example.demo.model;

/**
 * User record using Java 21 records feature
 * Records provide a compact syntax for declaring classes that are transparent holders for data
 */
public record User(
    String id,
    String name,
    String email,
    int age
) {
    /**
     * Compact constructor with validation
     */
    public User {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }

    /**
     * Custom method to get display name
     */
    public String displayName() {
        return name + " (" + email + ")";
    }

    /**
     * Check if user is adult
     */
    public boolean isAdult() {
        return age >= 18;
    }
}
