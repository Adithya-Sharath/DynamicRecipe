package com.recipeplanner.exceptions;

/**
 * Custom checked exception for authentication and authorization failures.
 * 
 * This exception is thrown when:
 * - User login credentials are invalid
 * - Username already exists during registration
 * - Password doesn't meet security requirements
 * - User account is not found
 * - Session validation fails
 * 
 * As a checked exception, callers must explicitly handle or declare it.
 * 
 * @author Recipe Planner Team
 * @version 1.0
 */
public class AuthenticationException extends Exception {
    
    /**
     * Constructs a new AuthenticationException with the specified detail message.
     * 
     * @param message The detail message explaining the authentication failure
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthenticationException with the specified detail message
     * and cause.
     * 
     * @param message The detail message
     * @param cause The underlying cause of the exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new AuthenticationException with a cause.
     * 
     * @param cause The underlying cause of the exception
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
