package com.recipeplanner.util;

import com.recipeplanner.model.*;
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
     * Seeds all initial data including demo user and sample recipes.
     * Demonstrates object creation and manipulation (Module 2).
     */
    public void seedAllData() {
        System.out.println("=== Seeding In-Memory Data ===");
        
        // Create demo users (demonstrates polymorphism - Module 5)
        createDemoUsers();
        
        // Create sample ingredients
        createSampleIngredients();
        
        // Create sample recipes
        createSampleRecipes();
        
        System.out.println("Data seeding complete!");
        System.out.println("- Users: " + repositoryManager.getUserRepository().count());
        System.out.println("- Ingredients: " + repositoryManager.getIngredientRepository().count());
        System.out.println("- Recipes: " + repositoryManager.getRecipeRepository().count());
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
     * Creates sample recipes using RecipeBuilder (demonstrates inner class - Module 6).
     * Uses String handling and Collections Framework.
     */
    private void createSampleRecipes() {
        // Recipe 1: Paneer Butter Masala (using RecipeBuilder - demonstrates inner class)
        Recipe paneerRecipe = new Recipe.RecipeBuilder("Paneer Butter Masala")
            .withCuisine("North Indian")
            .withCookingTime(45)
            .withDescription("Rich and creamy paneer curry")
            .createdBy(0) // System recipe
            .addInstruction("Heat ghee in a pan")
            .addInstruction("Add onions and sauté until golden")
            .addInstruction("Add tomato puree and spices")
            .addInstruction("Add paneer cubes and cream")
            .addInstruction("Simmer for 10 minutes and serve hot")
            .build();
        
        // Add ingredients to recipe
        paneerRecipe.setRawIngredientsText("paneer, tomatoes, onions, cream, ghee, garam masala, turmeric");
        
        // Add structured ingredients
        Ingredient paneerIng = repositoryManager.getIngredientRepository().findByName("paneer").orElse(new Ingredient("paneer"));
        paneerRecipe.addIngredient(new IngredientQuantity(paneerIng, new Measurement(250, "g")));
        
        repositoryManager.getRecipeRepository().save(paneerRecipe);
        
        // Recipe 2: Aloo Gobi
        Recipe alooGobiRecipe = new Recipe.RecipeBuilder("Aloo Gobi")
            .withCuisine("North Indian")
            .withCookingTime(30)
            .withDescription("Potato and cauliflower curry")
            .createdBy(0)
            .addInstruction("Cut potatoes and cauliflower into florets")
            .addInstruction("Heat oil and add cumin seeds")
            .addInstruction("Add vegetables and spices")
            .addInstruction("Cook covered for 15 minutes")
            .addInstruction("Garnish with coriander and serve")
            .build();
        
        alooGobiRecipe.setRawIngredientsText("potato, cauliflower, turmeric, cumin, coriander, ginger, garlic");
        
        Ingredient potatoIng = repositoryManager.getIngredientRepository().findByName("potato").orElse(new Ingredient("potato"));
        alooGobiRecipe.addIngredient(new IngredientQuantity(potatoIng, new Measurement(300, "g")));
        
        repositoryManager.getRecipeRepository().save(alooGobiRecipe);
        
        // Recipe 3: Dal Tadka
        Recipe dalRecipe = new Recipe.RecipeBuilder("Dal Tadka")
            .withCuisine("North Indian")
            .withCookingTime(40)
            .withDescription("Tempered lentil curry")
            .createdBy(0)
            .addInstruction("Pressure cook lentils with turmeric")
            .addInstruction("Prepare tadka with ghee, cumin, and garlic")
            .addInstruction("Pour tadka over cooked dal")
            .addInstruction("Garnish with coriander")
            .build();
        
        dalRecipe.setRawIngredientsText("lentils, ghee, cumin, garlic, turmeric, red chili powder");
        repositoryManager.getRecipeRepository().save(dalRecipe);
        
        // Recipe 4: Biryani
        Recipe biryaniRecipe = new Recipe.RecipeBuilder("Vegetable Biryani")
            .withCuisine("Hyderabadi")
            .withCookingTime(60)
            .withDescription("Aromatic rice dish with vegetables")
            .createdBy(0)
            .addInstruction("Marinate vegetables in yogurt and spices")
            .addInstruction("Cook basmati rice with whole spices")
            .addInstruction("Layer vegetables and rice")
            .addInstruction("Cook dum style for 20 minutes")
            .build();
        
        biryaniRecipe.setRawIngredientsText("rice, mixed vegetables, yogurt, garam masala, saffron, ghee");
        repositoryManager.getRecipeRepository().save(biryaniRecipe);
        
        // Recipe 5: Palak Paneer
        Recipe palakRecipe = new Recipe.RecipeBuilder("Palak Paneer")
            .withCuisine("North Indian")
            .withCookingTime(35)
            .withDescription("Spinach curry with cottage cheese")
            .createdBy(0)
            .addInstruction("Blanch spinach and make a puree")
            .addInstruction("Fry paneer cubes until golden")
            .addInstruction("Prepare gravy with onions and spices")
            .addInstruction("Add spinach puree and paneer")
            .addInstruction("Finish with cream")
            .build();
        
        palakRecipe.setRawIngredientsText("spinach, paneer, onions, tomatoes, cream, garam masala");
        repositoryManager.getRecipeRepository().save(palakRecipe);
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
