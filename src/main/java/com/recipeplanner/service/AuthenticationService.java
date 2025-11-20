package com.recipeplanner.service;

import com.recipeplanner.repository.UserRepository;
import com.recipeplanner.repository.RepositoryManager;
import com.recipeplanner.exceptions.AuthenticationException;
import com.recipeplanner.model.User;
import com.recipeplanner.model.RegularUser;
import com.recipeplanner.util.PasswordHasher;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for user authentication and registration.
 * Handles password validation, hashing, and user account management.
 * Demonstrates exception handling (Module 7) and service layer design.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Refactored for in-memory storage)
 */
public class AuthenticationService {

    private final UserRepository userRepository;

    /**
     * Constructor with UserRepository dependency injection.
     * 
     * @param userRepository The user repository
     */
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Default constructor that uses the singleton RepositoryManager.
     */
    public AuthenticationService() {
        this.userRepository = RepositoryManager.getInstance().getUserRepository();
    }

    /**
     * Registers a new user with the provided credentials.
     * Demonstrates EXCEPTION HANDLING (Module 7) with custom exceptions.
     * 
     * @param username The desired username
     * @param password The plain-text password
     * @return The newly created RegularUser object
     * @throws AuthenticationException if validation fails or username exists
     */
    public User register(String username, String password) 
            throws AuthenticationException {
        
        // Demonstrates exception handling with validation (Module 7)
        try {
            // Validate username
            if (username == null || username.trim().length() < 3) {
                throw new AuthenticationException(
                    "Username must be at least 3 characters long");
            }
            
            // Validate password
            if (password == null || password.length() < 6) {
                throw new AuthenticationException(
                    "Password must be at least 6 characters long");
            }
            
            // Additional password strength validation
            String passwordError = PasswordHasher.validatePasswordStrength(password);
            if (passwordError != null) {
                throw new AuthenticationException(passwordError);
            }
            
            // Check if username already exists
            Optional<User> existingUser = userRepository.findByUsername(username.trim());
            if (existingUser.isPresent()) {
                throw new AuthenticationException(
                    "Username '" + username + "' is already taken");
            }
            
            // Hash password (demonstrates utility class usage)
            String hashedPassword = PasswordHasher.hashPasswordWithNewSalt(password);
            
            // Create RegularUser object (demonstrates polymorphism - Module 5)
            RegularUser newUser = new RegularUser(username.trim(), hashedPassword);
            
            // Save to repository
            userRepository.save(newUser);
            
            return newUser;
            
        } catch (AuthenticationException e) {
            // Rethrow authentication exceptions
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions with context
            throw new AuthenticationException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticates a user with username and password.
     * Demonstrates EXCEPTION HANDLING (Module 7).
     * 
     * @param username The username
     * @param password The plain text password
     * @return The authenticated User object
     * @throws AuthenticationException if credentials are invalid
     */
    public User login(String username, String password) 
            throws AuthenticationException {
        
        // Demonstrates exception handling with try-catch (Module 7)
        try {
            // Validate inputs
            if (username == null || username.trim().isEmpty()) {
                throw new AuthenticationException("Username cannot be empty");
            }
            
            if (password == null || password.isEmpty()) {
                throw new AuthenticationException("Password cannot be empty");
            }
            
            // Find user by username
            Optional<User> userOpt = userRepository.findByUsername(username.trim());
            
            if (!userOpt.isPresent()) {
                throw new AuthenticationException("Invalid username or password");
            }
            
            User user = userOpt.get();
            
            // Verify password
            boolean passwordValid = PasswordHasher.verifyPassword(
                password, 
                user.getPasswordHash()
            );
            
            if (!passwordValid) {
                throw new AuthenticationException("Invalid username or password");
            }
            
            // Update last login timestamp
            user.setLastLogin(LocalDateTime.now());
            userRepository.update(user);
            
            return user;
            
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Login failed: " + e.getMessage(), e);
        }
    }

    /**
     * Changes a user's password.
     * Demonstrates exception handling (Module 7).
     * 
     * @param userId The user ID
     * @param currentPassword The current password
     * @param newPassword The new password
     * @throws AuthenticationException if current password is incorrect or new password is invalid
     */
    public void changePassword(int userId, String currentPassword, String newPassword) 
            throws AuthenticationException {
        
        try {
            // Validate new password
            String passwordError = PasswordHasher.validatePasswordStrength(newPassword);
            if (passwordError != null) {
                throw new AuthenticationException(passwordError);
            }
            
            // Find user
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                throw new AuthenticationException("User not found");
            }
            
            User user = userOpt.get();
            
            // Verify current password
            boolean passwordValid = PasswordHasher.verifyPassword(
                currentPassword, 
                user.getPasswordHash()
            );
            
            if (!passwordValid) {
                throw new AuthenticationException("Current password is incorrect");
            }
            
            // Hash new password
            String hashedPassword = PasswordHasher.hashPasswordWithNewSalt(newPassword);
            
            // Update password
            user.setPasswordHash(hashedPassword);
            userRepository.update(user);
            
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Password change failed: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a username is available (not taken).
     * 
     * @param username The username to check
     * @return true if username is available
     */
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        Optional<User> existingUser = userRepository.findByUsername(username.trim());
        return !existingUser.isPresent();
    }

    /**
     * Retrieves a user by ID.
     * 
     * @param userId The user ID
     * @return The User object, or null if not found
     */
    public User getUserById(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(null);
    }

    /**
     * Updates a user's profile information.
     * Does not update password - use changePassword for that.
     * 
     * @param user The user with updated information
     */
    public void updateUserProfile(User user) {
        userRepository.update(user);
    }
}
