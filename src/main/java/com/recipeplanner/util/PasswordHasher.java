package com.recipeplanner.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for secure password hashing and verification.
 * Uses SHA-256 hashing algorithm with salt for enhanced security.
 * 
 * Password Storage Format: "salt:hash"
 * - Salt: Random 16-byte value (Base64 encoded)
 * - Hash: SHA-256(password + salt)
 * 
 * Usage Example:
 * <pre>
 * // Hash a password
 * String hashedPassword = PasswordHasher.hashPassword("myPassword123", 
 *                         PasswordHasher.generateSalt());
 * 
 * // Verify a password
 * boolean isValid = PasswordHasher.verifyPassword("myPassword123", hashedPassword);
 * </pre>
 * 
 * Security Notes:
 * - Each password gets a unique random salt
 * - SHA-256 provides strong one-way hashing
 * - Rainbow table attacks are prevented by salt
 * - Password is never stored in plain text
 * 
 * @author Recipe Planner Team
 * @version 1.0
 */
public class PasswordHasher {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16; // 16 bytes = 128 bits
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private PasswordHasher() {
        throw new UnsupportedOperationException("PasswordHasher is a utility class and cannot be instantiated");
    }

    /**
     * Generates a cryptographically strong random salt.
     * The salt is Base64 encoded for easy storage.
     * 
     * @return A Base64-encoded random salt string
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password with the given salt using SHA-256.
     * Returns the result in "salt:hash" format for database storage.
     * 
     * @param password The plain text password to hash
     * @param salt The salt to use for hashing
     * @return The salted and hashed password in "salt:hash" format
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
    public static String hashPassword(String password, String salt) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }

        try {
            // Combine password and salt
            String saltedPassword = password + salt;
            
            // Get SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            
            // Hash the salted password
            byte[] hashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            
            // Encode hash to Base64
            String hash = Base64.getEncoder().encodeToString(hashBytes);
            
            // Return in "salt:hash" format
            return salt + ":" + hash;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Convenience method to hash a password with a newly generated salt.
     * 
     * @param password The plain text password to hash
     * @return The salted and hashed password in "salt:hash" format
     */
    public static String hashPasswordWithNewSalt(String password) {
        String salt = generateSalt();
        return hashPassword(password, salt);
    }

    /**
     * Verifies a plain text password against a stored hash.
     * 
     * @param password The plain text password to verify
     * @param storedHash The stored hash in "salt:hash" format
     * @return true if the password matches the stored hash
     * @throws IllegalArgumentException if storedHash format is invalid
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null) {
            return false;
        }
        if (storedHash == null || !storedHash.contains(":")) {
            throw new IllegalArgumentException("Invalid stored hash format. Expected 'salt:hash'");
        }

        try {
            // Extract salt from stored hash
            String[] parts = storedHash.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid stored hash format. Expected 'salt:hash'");
            }
            
            String salt = parts[0];
            String originalHash = parts[1];
            
            // Hash the input password with the extracted salt
            String testHash = hashPassword(password, salt);
            
            // Extract just the hash part for comparison
            String[] testParts = testHash.split(":", 2);
            String testHashValue = testParts[1];
            
            // Compare hashes using constant-time comparison
            return constantTimeEquals(originalHash, testHashValue);
            
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Performs constant-time string comparison to prevent timing attacks.
     * 
     * @param a First string
     * @param b Second string
     * @return true if strings are equal
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return a == b;
        }
        
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        
        if (aBytes.length != bBytes.length) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        
        return result == 0;
    }

    /**
     * Extracts the salt from a stored password hash.
     * 
     * @param storedHash The stored hash in "salt:hash" format
     * @return The salt component
     * @throws IllegalArgumentException if format is invalid
     */
    public static String extractSalt(String storedHash) {
        if (storedHash == null || !storedHash.contains(":")) {
            throw new IllegalArgumentException("Invalid stored hash format. Expected 'salt:hash'");
        }
        String[] parts = storedHash.split(":", 2);
        return parts[0];
    }

    /**
     * Checks if a password meets minimum security requirements.
     * 
     * Requirements:
     * - At least 6 characters long
     * - Contains at least one letter
     * - Contains at least one digit
     * 
     * @param password The password to validate
     * @return true if password meets requirements
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        
        return hasLetter && hasDigit;
    }

    /**
     * Validates password strength and returns a message if invalid.
     * 
     * @param password The password to validate
     * @return null if valid, error message if invalid
     */
    public static String validatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Password must contain at least one letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        return null; // Password is valid
    }
}
