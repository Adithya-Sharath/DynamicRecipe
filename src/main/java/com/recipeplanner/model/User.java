package com.recipeplanner.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a user in the Recipe & Meal Planner application.
 * Base class for user types - demonstrates INHERITANCE (Module 5).
 * 
 * This class encapsulates user authentication and profile information.
 * Subclasses include RegularUser and AdminUser which override behavior
 * to demonstrate POLYMORPHISM (Module 5).
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Refactored for in-memory storage)
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    /**
     * Default constructor for User.
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Parameterized constructor for User.
     * 
     * @param username The unique username
     * @param passwordHash The hashed password (salt:hash format)
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Full constructor including ID and timestamps.
     * 
     * @param id The user ID
     * @param username The unique username
     * @param passwordHash The hashed password
     * @param createdAt Account creation timestamp
     * @param lastLogin Last login timestamp
     */
    public User(int id, String username, String passwordHash, 
                LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Checks if this is a newly created user (not yet persisted).
     * 
     * @return true if user has not been saved yet
     */
    public boolean isNew() {
        return this.id == 0;
    }
    
    /**
     * Checks if this user has admin privileges.
     * Base implementation returns false - subclasses can override.
     * Demonstrates POLYMORPHISM (Module 5).
     * 
     * @return true if user is an admin
     */
    public boolean isAdmin() {
        return false; // Base User class has no admin privileges
    }
    
    /**
     * Gets the type of user as a string.
     * Base implementation returns "User" - subclasses override for specific types.
     * Demonstrates POLYMORPHISM (Module 5).
     * 
     * @return The user type
     */
    public String getUserType() {
        return "User";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
