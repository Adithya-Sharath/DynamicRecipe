package com.recipeplanner.repository;

/**
 * Singleton manager class that provides centralized access to all repositories.
 * Demonstrates Singleton design pattern and centralized data management.
 * 
 * This class ensures that there is only one instance of each repository
 * throughout the application lifecycle, maintaining data consistency.
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public class RepositoryManager {
    
    // Singleton instance
    private static RepositoryManager instance;
    
    // Repository instances (demonstrates encapsulation)
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    
    /**
     * Private constructor to prevent external instantiation.
     * Demonstrates Singleton pattern and encapsulation.
     */
    private RepositoryManager() {
        this.userRepository = new UserRepository();
        this.recipeRepository = new RecipeRepository();
        this.ingredientRepository = new IngredientRepository();
    }
    
    /**
     * Gets the singleton instance of RepositoryManager.
     * Creates the instance if it doesn't exist (lazy initialization).
     * 
     * @return The singleton RepositoryManager instance
     */
    public static synchronized RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }
    
    /**
     * Gets the UserRepository instance.
     * 
     * @return UserRepository
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    /**
     * Gets the RecipeRepository instance.
     * 
     * @return RecipeRepository
     */
    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }
    
    /**
     * Gets the IngredientRepository instance.
     * 
     * @return IngredientRepository
     */
    public IngredientRepository getIngredientRepository() {
        return ingredientRepository;
    }
    
    /**
     * Resets all repositories (clears all data).
     * Useful for testing or application reset.
     */
    public void resetAll() {
        userRepository.clear();
        recipeRepository.clear();
        ingredientRepository.clear();
    }
    
    /**
     * Resets the singleton instance.
     * Use with caution - primarily for testing.
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.resetAll();
            instance = null;
        }
    }
}
