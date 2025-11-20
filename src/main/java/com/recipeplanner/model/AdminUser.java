package com.recipeplanner.model;

import java.time.LocalDateTime;

/**
 * Represents an administrator user in the Recipe & Meal Planner application.
 * Extends User class to demonstrate INHERITANCE and POLYMORPHISM (Module 5).
 * 
 * Admin users have elevated permissions:
 * - Can manage all recipes (including seeded ones)
 * - Can view all meal plans
 * - Can manage user accounts
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public class AdminUser extends User {
    
    private String adminLevel; // e.g., "Super Admin", "Moderator"
    private int totalSystemRecipes;
    
    /**
     * Default constructor for AdminUser.
     */
    public AdminUser() {
        super();
        this.adminLevel = "Moderator";
    }
    
    /**
     * Constructor with username and password.
     * 
     * @param username The username
     * @param passwordHash The hashed password
     */
    public AdminUser(String username, String passwordHash) {
        super(username, passwordHash);
        this.adminLevel = "Moderator";
    }
    
    /**
     * Full constructor.
     * 
     * @param id User ID
     * @param username Username
     * @param passwordHash Hashed password
     * @param createdAt Account creation timestamp
     * @param lastLogin Last login timestamp
     * @param adminLevel The admin privilege level
     */
    public AdminUser(int id, String username, String passwordHash, 
                    LocalDateTime createdAt, LocalDateTime lastLogin, String adminLevel) {
        super(id, username, passwordHash, createdAt, lastLogin);
        this.adminLevel = adminLevel;
    }
    
    /**
     * Demonstrates POLYMORPHISM - Overriding base class method (Module 5).
     * Admin users have elevated permissions.
     * 
     * @return true - admin users have admin rights
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
    
    /**
     * Demonstrates POLYMORPHISM - Method overriding (Module 5).
     * Returns the user type with admin level.
     * 
     * @return "Admin User (level)"
     */
    @Override
    public String getUserType() {
        return "Admin User (" + adminLevel + ")";
    }
    
    /**
     * Checks if this admin can delete a recipe.
     * Admins can delete any recipe.
     * 
     * @param recipe The recipe to check
     * @return true - admins can delete any recipe
     */
    public boolean canDeleteRecipe(Recipe recipe) {
        return true; // Admins can delete any recipe
    }
    
    /**
     * Checks if this admin can edit a recipe.
     * Admins can edit any recipe.
     * 
     * @param recipe The recipe to check
     * @return true - admins can edit any recipe
     */
    public boolean canEditRecipe(Recipe recipe) {
        return true; // Admins can edit any recipe
    }
    
    /**
     * Checks if this admin can manage users.
     * Only Super Admins can manage users.
     * 
     * @return true if admin level is "Super Admin"
     */
    public boolean canManageUsers() {
        return "Super Admin".equalsIgnoreCase(adminLevel);
    }
    
    // Getters and Setters
    
    public String getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    public int getTotalSystemRecipes() {
        return totalSystemRecipes;
    }
    
    public void setTotalSystemRecipes(int totalSystemRecipes) {
        this.totalSystemRecipes = totalSystemRecipes;
    }
    
    @Override
    public String toString() {
        return "AdminUser{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", adminLevel='" + adminLevel + '\'' +
                ", totalSystemRecipes=" + totalSystemRecipes +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
