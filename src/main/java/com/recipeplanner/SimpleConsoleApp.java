package com.recipeplanner;

import com.recipeplanner.model.*;
import com.recipeplanner.service.AuthenticationService;
import com.recipeplanner.service.RecipeService;
import com.recipeplanner.util.InMemoryDataSeeder;
import com.recipeplanner.exceptions.AuthenticationException;

import java.util.List;
import java.util.Scanner;

/**
 * Simple console application to demonstrate the refactored in-memory Recipe Planner.
 * This demonstrates all the core functionality without JavaFX complexity.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Console Demo)
 */
public class SimpleConsoleApp {
    
    private static AuthenticationService authService = new AuthenticationService();
    private static RecipeService recipeService = new RecipeService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Recipe & Meal Planner - Console Edition     ║");
        System.out.println("║   Demonstrating In-Memory OOP Implementation  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
        
        // Initialize in-memory data
        initializeData();
        
        // Login
        if (login()) {
            // Show main menu
            mainMenu();
        }
        
        scanner.close();
        System.out.println("\nThank you for using Recipe & Meal Planner!");
    }
    
    /**
     * Initializes in-memory data storage.
     * Demonstrates data seeding and initialization.
     */
    private static void initializeData() {
        System.out.println("Initializing in-memory data storage...");
        
        try {
            InMemoryDataSeeder seeder = new InMemoryDataSeeder();
            seeder.seedAllData();
            System.out.println("✓ Data initialization complete!\n");
        } catch (Exception e) {
            System.err.println("✗ Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handles user login.
     * Demonstrates polymorphism (RegularUser vs AdminUser) and exception handling.
     */
    private static boolean login() {
        System.out.println("═══ LOGIN ═══");
        System.out.println("Demo Accounts:");
        System.out.println("  Regular User: demo / demo123");
        System.out.println("  Admin User:   admin / admin123");
        System.out.println();
        
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            try {
                // Demonstrates exception handling (Module 7)
                currentUser = authService.login(username, password);
                
                System.out.println("\n✓ Login successful!");
                System.out.println("Welcome, " + currentUser.getUsername());
                
                // Demonstrates polymorphism (Module 5)
                System.out.println("User Type: " + currentUser.getUserType());
                System.out.println("Admin Privileges: " + (currentUser.isAdmin() ? "Yes" : "No"));
                System.out.println();
                
                return true;
                
            } catch (AuthenticationException e) {
                System.out.println("✗ Login failed: " + e.getMessage());
                System.out.println("Attempts remaining: " + (2 - attempts) + "\n");
            }
        }
        
        System.out.println("Too many failed attempts. Exiting.");
        return false;
    }
    
    /**
     * Displays main menu and handles user interaction.
     * Demonstrates Collections Framework usage.
     */
    private static void mainMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          MAIN MENU                     ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. View All Recipes");
            System.out.println("2. Search Recipes");
            System.out.println("3. View Recipe Details");
            System.out.println("4. View My Recipes");
            System.out.println("5. Recipe Statistics");
            System.out.println("6. Logout");
            System.out.print("\nChoice: ");
            
            String choice = scanner.nextLine().trim();
            System.out.println();
            
            switch (choice) {
                case "1":
                    viewAllRecipes();
                    break;
                case "2":
                    searchRecipes();
                    break;
                case "3":
                    viewRecipeDetails();
                    break;
                case "4":
                    viewMyRecipes();
                    break;
                case "5":
                    showStatistics();
                    break;
                case "6":
                    running = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays all recipes.
     * Demonstrates ArrayList usage and Collections Framework (Module 4).
     */
    private static void viewAllRecipes() {
        System.out.println("═══ ALL RECIPES ═══");
        
        List<Recipe> recipes = recipeService.getAllRecipes();
        
        if (recipes.isEmpty()) {
            System.out.println("No recipes available.");
            return;
        }
        
        System.out.println("Total: " + recipes.size() + " recipes\n");
        
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            System.out.printf("%d. %s (%s) - %s%n",
                i + 1,
                recipe.getName(),
                recipe.getCuisine(),
                recipe.getFormattedTime());
        }
    }
    
    /**
     * Searches for recipes.
     * Demonstrates String handling (Module 3) and interface implementation (Module 6).
     */
    private static void searchRecipes() {
        System.out.println("═══ SEARCH RECIPES ═══");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty.");
            return;
        }
        
        List<Recipe> results = recipeService.searchRecipes(searchTerm, "name");
        
        System.out.println("\nSearch Results: " + results.size() + " recipe(s) found\n");
        
        for (int i = 0; i < results.size(); i++) {
            Recipe recipe = results.get(i);
            // Demonstrates interface usage - Searchable
            System.out.printf("%d. %s%n", i + 1, recipe.getSearchDisplayText());
        }
    }
    
    /**
     * Displays detailed information about a specific recipe.
     */
    private static void viewRecipeDetails() {
        System.out.println("═══ RECIPE DETAILS ═══");
        System.out.print("Enter recipe number: ");
        String input = scanner.nextLine().trim();
        
        try {
            int recipeId = Integer.parseInt(input);
            Recipe recipe = recipeService.getRecipeById(recipeId);
            
            if (recipe == null) {
                System.out.println("Recipe not found.");
                return;
            }
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Recipe: " + recipe.getName());
            System.out.println("=".repeat(50));
            System.out.println("Cuisine: " + recipe.getCuisine());
            System.out.println("Cooking Time: " + recipe.getFormattedTime());
            System.out.println("Ingredients: " + recipe.getIngredientCount());
            
            if (recipe.getRawIngredientsText() != null) {
                System.out.println("\nIngredients List:");
                System.out.println(recipe.getRawIngredientsText());
            }
            
            System.out.println("\nInstructions:");
            List<String> instructions = recipe.getInstructions();
            for (int i = 0; i < instructions.size(); i++) {
                System.out.println((i + 1) + ". " + instructions.get(i));
            }
            System.out.println("=".repeat(50));
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid recipe number.");
        }
    }
    
    /**
     * Displays recipes created by the current user.
     */
    private static void viewMyRecipes() {
        System.out.println("═══ MY RECIPES ═══");
        
        List<Recipe> myRecipes = recipeService.getUserRecipes(currentUser.getId());
        
        if (myRecipes.isEmpty()) {
            System.out.println("You haven't created any recipes yet.");
            return;
        }
        
        System.out.println("Total: " + myRecipes.size() + " recipe(s)\n");
        
        for (int i = 0; i < myRecipes.size(); i++) {
            Recipe recipe = myRecipes.get(i);
            System.out.printf("%d. %s (%s)%n",
                i + 1,
                recipe.getName(),
                recipe.getCuisine());
        }
    }
    
    /**
     * Displays recipe statistics.
     */
    private static void showStatistics() {
        System.out.println("═══ STATISTICS ═══");
        
        String summary = recipeService.getRecipeSummary();
        System.out.println(summary);
        
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        
        // Count by cuisine (demonstrates Collections usage)
        System.out.println("\nRecipes by Cuisine:");
        System.out.println("- North Indian: " + countByCuisine(allRecipes, "North Indian"));
        System.out.println("- Hyderabadi: " + countByCuisine(allRecipes, "Hyderabadi"));
    }
    
    /**
     * Helper method to count recipes by cuisine.
     * Demonstrates String comparison and filtering.
     */
    private static int countByCuisine(List<Recipe> recipes, String cuisine) {
        int count = 0;
        for (Recipe recipe : recipes) {
            if (cuisine.equalsIgnoreCase(recipe.getCuisine())) {
                count++;
            }
        }
        return count;
    }
}
