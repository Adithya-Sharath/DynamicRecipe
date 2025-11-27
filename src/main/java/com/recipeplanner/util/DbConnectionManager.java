package com.recipeplanner.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple database connection manager for MySQL.
 * Provides centralized JDBC connection handling.
 * 
 * SETUP INSTRUCTIONS:
 * 1. Install MySQL and start the service
 * 2. Create database: CREATE DATABASE dynamic_recipe;
 * 3. Update USER and PASSWORD constants below with your MySQL credentials
 * 4. Run the application - it will auto-load recipes on first run
 * 
 * @author Recipe Planner Team
 * @version 3.0 (MySQL Integration)
 */
public class DbConnectionManager {
    
    // ========== CONFIGURE THESE FOR YOUR MYSQL SETUP ==========
    private static final String URL = 
        "jdbc:mysql://localhost:3306/dynamic_recipe?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";           // MySQL username
    private static final String PASSWORD = "root";       // MySQL password
    // ===========================================================
    
    /**
     * Gets a database connection.
     * 
     * @return Active MySQL connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver (required for some Java versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Check pom.xml dependencies.", e);
        }
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Tests if database connection is working.
     * 
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("   Check:");
            System.err.println("   1. MySQL is running");
            System.err.println("   2. Database 'dynamic_recipe' exists");
            System.err.println("   3. Username/password in DbConnectionManager are correct");
            return false;
        }
    }
}
