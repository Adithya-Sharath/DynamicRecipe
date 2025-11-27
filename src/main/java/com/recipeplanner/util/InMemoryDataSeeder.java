package com.recipeplanner.util;

import com.recipeplanner.model.*;
import com.recipeplanner.repository.RecipeRepository;
import com.recipeplanner.repository.RepositoryManager;

/**
 * Utility class to seed initial data into in-memory repositories.
 * Demonstrates object creation, collections usage, and data initialization.
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public class InMemoryDataSeeder {
    
    private final RepositoryManager repositoryManager;
    
    /**
     * Constructor initializes with RepositoryManager.
     */
    public InMemoryDataSeeder() {
        this.repositoryManager = RepositoryManager.getInstance();
    }
    
    /**
     * Seeds all initial data including demo users and loads recipes from MySQL.
     * Auto-loads CSV recipes into database on first run.
     * Demonstrates object creation and manipulation (Module 2).
     */
    public void seedAllData() {
        System.out.println("=== Initializing Data ===");
        
        // Test database connection first
        if (!DbConnectionManager.testConnection()) {
            System.err.println("\n⚠️  WARNING: MySQL database connection failed!");
            System.err.println("The application will not work properly without database access.");
            System.err.println("Please check DbConnectionManager.java for setup instructions.\n");
            return;
        }
        
        System.out.println("✓ Database connection successful");
        
        // Create demo users (demonstrates polymorphism - Module 5)
        createDemoUsers();
        
        // Create sample ingredients (in-memory)
        createSampleIngredients();
        
        // Load recipes from CSV into MySQL (only on first run)
        loadRecipesFromCsv();
        
        System.out.println("\n=== Data Initialization Complete ===");
        System.out.println("- Users: " + repositoryManager.getUserRepository().count());
        System.out.println("- Ingredients: " + repositoryManager.getIngredientRepository().count());
        System.out.println("- Recipes in MySQL: " + repositoryManager.getRecipeRepository().count());
    }
    
    /**
     * Loads recipes from CSV into MySQL database.
     * Only runs on first application start when database is empty.
     */
    private void loadRecipesFromCsv() {
        RecipeRepository recipeRepo = repositoryManager.getRecipeRepository();
        
        // Check if database already has recipes
        int existingCount = recipeRepo.count();
        
        if (existingCount > 0) {
            System.out.println("✓ Database already contains " + existingCount + " recipes");
            return;
        }
        
        // Database is empty - load from CSV
        System.out.println("\nDatabase is empty - loading recipes from CSV...");
        System.out.println("This is a one-time operation and may take a minute...");
        
        long startTime = System.currentTimeMillis();
        int loadedCount = CSVRecipeLoader.loadRecipesFromCSV(recipeRepo);
        long endTime = System.currentTimeMillis();
        
        System.out.println("✓ Successfully loaded " + loadedCount + " recipes into MySQL");
        System.out.println("  Time taken: " + (endTime - startTime) / 1000.0 + " seconds");
    }
    
    /**
     * Creates demo users including regular user and admin.
     * Demonstrates POLYMORPHISM with User subclasses (Module 5).
     */
    public void createDemoUsers() {
        try {
            // Create demo regular user (demonstrates polymorphism - Module 5)
            String demoPassword = PasswordHasher.hashPasswordWithNewSalt("demo123");
            RegularUser demoUser = new RegularUser("demo", demoPassword);
            repositoryManager.getUserRepository().save(demoUser);
            System.out.println("Created demo user: demo/demo123");
            
            // Create admin user (demonstrates polymorphism - Module 5)
            String adminPassword = PasswordHasher.hashPasswordWithNewSalt("admin123");
            AdminUser adminUser = new AdminUser("admin", adminPassword);
            adminUser.setAdminLevel("Super Admin");
            repositoryManager.getUserRepository().save(adminUser);
            System.out.println("Created admin user: admin/admin123");
            
        } catch (Exception e) {
            System.err.println("Error creating demo users: " + e.getMessage());
        }
    }
    
    /**
     * Creates sample ingredients.
     * Demonstrates ArrayList usage and object creation (Modules 2, 4).
     */
    public void createSampleIngredients() {
        // Demonstrates array usage with String[] (Module 3)
        String[] spices = {"turmeric", "cumin", "coriander", "garam masala", "red chili powder"};
        String[] vegetables = {"onion", "tomato", "potato", "garlic", "ginger"};
        String[] dairy = {"ghee", "yogurt", "paneer", "cream", "milk"};
        
        // Create spices
        for (String spice : spices) {
            Ingredient ingredient = new Ingredient(spice, "Spices");
            repositoryManager.getIngredientRepository().save(ingredient);
        }
        
        // Create vegetables
        for (String veg : vegetables) {
            Ingredient ingredient = new Ingredient(veg, "Vegetables");
            repositoryManager.getIngredientRepository().save(ingredient);
        }
        
        // Create dairy
        for (String item : dairy) {
            Ingredient ingredient = new Ingredient(item, "Dairy");
            repositoryManager.getIngredientRepository().save(ingredient);
        }
    }
    
    /**
     * Checks if data has already been seeded.
     * 
     * @return true if repositories contain data
     */
    public boolean isDataSeeded() {
        return repositoryManager.getUserRepository().count() > 0 ||
               repositoryManager.getRecipeRepository().count() > 0;
    }
}
