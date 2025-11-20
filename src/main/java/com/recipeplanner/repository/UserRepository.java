package com.recipeplanner.repository;

import com.recipeplanner.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository for User entities using ArrayList.
 * Demonstrates Collections Framework (Module 4) and Access Control (Module 4).
 * 
 * This class replaces the MySQL-based UserDAOImpl with a simple
 * ArrayList-based storage mechanism suitable for a college-level project.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (In-Memory)
 */
public class UserRepository {
    
    // Demonstrates Collections Framework - ArrayList usage
    private final List<User> users;
    private int nextId;
    
    /**
     * Constructor initializes the in-memory storage.
     * Demonstrates object initialization and constructor usage.
     */
    public UserRepository() {
        this.users = new ArrayList<>();
        this.nextId = 1;
    }
    
    /**
     * Finds a user by their unique ID.
     * Demonstrates linear search through ArrayList.
     * 
     * @param id The user ID
     * @return Optional containing the User if found, empty otherwise
     */
    public Optional<User> findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Finds a user by their unique username.
     * Demonstrates String handling and comparison (Module 3).
     * 
     * @param username The username to search for
     * @return Optional containing the User if found, empty otherwise
     */
    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        
        // Demonstrates String handling with trim() and equalsIgnoreCase()
        String normalizedUsername = username.trim();
        
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(normalizedUsername)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Saves a new user to the repository.
     * Auto-generates ID if not set.
     * 
     * @param user The user to save
     * @return The saved user with ID assigned
     */
    public User save(User user) {
        if (user.getId() == 0) {
            user.setId(nextId++);
        }
        
        // Check if updating existing user
        Optional<User> existing = findById(user.getId());
        if (existing.isPresent()) {
            // Update existing user
            int index = users.indexOf(existing.get());
            users.set(index, user);
        } else {
            // Add new user
            users.add(user);
        }
        
        return user;
    }
    
    /**
     * Updates an existing user's information.
     * 
     * @param user The user with updated information
     * @return true if update was successful
     */
    public boolean update(User user) {
        Optional<User> existing = findById(user.getId());
        if (existing.isPresent()) {
            int index = users.indexOf(existing.get());
            users.set(index, user);
            return true;
        }
        return false;
    }
    
    /**
     * Deletes a user from the repository.
     * 
     * @param userId The ID of the user to delete
     * @return true if deletion was successful
     */
    public boolean delete(int userId) {
        return users.removeIf(user -> user.getId() == userId);
    }
    
    /**
     * Gets all users in the repository.
     * Demonstrates returning Collections.
     * 
     * @return List of all users
     */
    public List<User> findAll() {
        return new ArrayList<>(users); // Return a copy to prevent external modification
    }
    
    /**
     * Gets the total count of users.
     * 
     * @return The number of users in the repository
     */
    public int count() {
        return users.size();
    }
    
    /**
     * Clears all users from the repository.
     * Useful for testing or reset operations.
     */
    public void clear() {
        users.clear();
        nextId = 1;
    }
}
