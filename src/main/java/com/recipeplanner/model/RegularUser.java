package com.recipeplanner.model;

import java.time.LocalDateTime;

/**
 * Represents a regular user in the Recipe & Meal Planner application.
 * Extends User class to demonstrate INHERITANCE (Module 5).
 * 
 * Regular users have standard permissions:
 * - Can create and manage their own recipes
 * - Can create personal meal plans
 * - Cannot delete system/seeded recipes
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public class RegularUser extends User {
    
    private int recipeCount;
    private int mealPlanCount;
    
    /**
     * Default constructor for RegularUser.
     */
    public RegularUser() {
        super();
    }
    
    /**
     * Constructor with username and password.
     * 
     * @param username The username
     * @param passwordHash The hashed password
     */
    public RegularUser(String username, String passwordHash) {
        super(username, passwordHash);
    }
    
    /**
     * Full constructor.
     * 
     * @param id User ID
     * @param username Username
     * @param passwordHash Hashed password
     * @param createdAt Account creation timestamp
     * @param lastLogin Last login timestamp
     */
    public RegularUser(int id, String username, String passwordHash, 
                      LocalDateTime createdAt, LocalDateTime lastLogin) {
        super(id, username, passwordHash, createdAt, lastLogin);
    }
    
    /**
     * Demonstrates POLYMORPHISM - Overriding base class method (Module 5).
     * Regular users have standard permissions.
     * 
     * @return false - regular users are not admins
     */
    @Override
    public boolean isAdmin() {
        return false;
    }
    
    /**
     * Demonstrates POLYMORPHISM - Method overriding (Module 5).
     * Returns the user type as a string.
     * 
     * @return "Regular User"
     */
    @Override
    public String getUserType() {
        return "Regular User";
    }
    
    /**
     * Checks if this user can delete a recipe.
     * Regular users can only delete their own recipes.
     * 
     * @param recipe The recipe to check
     * @return true if user can delete the recipe
     */
    public boolean canDeleteRecipe(Recipe recipe) {
        // Regular users can only delete their own recipes (not seeded ones)
        return recipe.getCreatedBy() == this.getId() && !recipe.isSeededRecipe();
    }
    
    /**
     * Checks if this user can edit a recipe.
     * Regular users can only edit their own recipes.
     * 
     * @param recipe The recipe to check
     * @return true if user can edit the recipe
     */
    public boolean canEditRecipe(Recipe recipe) {
        return recipe.getCreatedBy() == this.getId();
    }
    
    // Getters and Setters for additional fields
    
    public int getRecipeCount() {
        return recipeCount;
    }
    
    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }
    
    public int getMealPlanCount() {
        return mealPlanCount;
    }
    
    public void setMealPlanCount(int mealPlanCount) {
        this.mealPlanCount = mealPlanCount;
    }
    
    /**
     * Increments the recipe count.
     */
    public void incrementRecipeCount() {
        this.recipeCount++;
    }
    
    /**
     * Increments the meal plan count.
     */
    public void incrementMealPlanCount() {
        this.mealPlanCount++;
    }
    
    @Override
    public String toString() {
        return "RegularUser{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", recipeCount=" + recipeCount +
                ", mealPlanCount=" + mealPlanCount +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
