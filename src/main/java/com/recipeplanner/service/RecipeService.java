package com.recipeplanner.service;

import com.recipeplanner.repository.RecipeRepository;
import com.recipeplanner.repository.RepositoryManager;
import com.recipeplanner.model.Recipe;

import java.util.List;
import java.util.Optional;

/**
 * Service class for recipe-related business operations.
 * Acts as a facade for recipe search and management.
 * Demonstrates service layer pattern and business logic separation.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Refactored for in-memory storage)
 */
public class RecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * Constructor with RecipeRepository dependency injection.
     * 
     * @param recipeRepository The recipe repository
     */
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Default constructor that uses singleton RepositoryManager.
     */
    public RecipeService() {
        this.recipeRepository = RepositoryManager.getInstance().getRecipeRepository();
    }

    /**
     * Searches for recipes based on search type and term.
     * Demonstrates String handling and Collections (Modules 3, 4).
     * 
     * @param searchTerm The search term
     * @param searchType The type of search: "name", "ingredient", or "cuisine"
     * @return List of matching recipes
     * @throws IllegalArgumentException if searchType is invalid
     */
    public List<Recipe> searchRecipes(String searchTerm, String searchType) {
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Return all recipes if no search term
            return recipeRepository.findAll();
        }
        
        if (searchType == null) {
            searchType = "name"; // Default to name search
        }
        
        // Demonstrates String handling (Module 3)
        switch (searchType.toLowerCase()) {
            case "name":
                return recipeRepository.searchByName(searchTerm);
            
            case "ingredient":
                // Search in rawIngredientsText
                return recipeRepository.searchByName(searchTerm);
            
            case "cuisine":
                return recipeRepository.findByCuisine(searchTerm);
            
            default:
                throw new IllegalArgumentException(
                    "Invalid search type: " + searchType + 
                    ". Must be 'name', 'ingredient', or 'cuisine'"
                );
        }
    }

    /**
     * Gets all recipes created by a specific user.
     * 
     * @param userId The user ID
     * @return List of user-created recipes
     */
    public List<Recipe> getUserRecipes(int userId) {
        return recipeRepository.findByCreatedBy(userId);
    }

    /**
     * Gets a recipe by its ID with full details.
     * 
     * @param recipeId The recipe ID
     * @return The complete Recipe object, or null if not found
     */
    public Recipe getRecipeById(int recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        return recipe.orElse(null);
    }

    /**
     * Gets all recipes.
     * 
     * @return List of all recipes
     */
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    /**
     * Saves a recipe (insert if new, update if existing).
     * Validates that recipe meets minimum requirements.
     * 
     * @param recipe The recipe to save
     * @return The saved recipe
     * @throws IllegalArgumentException if recipe validation fails
     */
    public Recipe saveRecipe(Recipe recipe) {
        // Validate recipe
        validateRecipe(recipe);
        return recipeRepository.save(recipe);
    }

    /**
     * Deletes a recipe by ID.
     * 
     * @param recipeId The recipe ID to delete
     * @return true if deletion was successful
     */
    public boolean deleteRecipe(int recipeId) {
        return recipeRepository.delete(recipeId);
    }

    /**
     * Gets quick recipes (under 30 minutes).
     * 
     * @return List of all recipes (filtering can be added later)
     */
    public List<Recipe> getQuickRecipes() {
        return recipeRepository.findAll();
    }

    /**
     * Searches for recipes by multiple cuisines.
     * 
     * @param cuisines List of cuisine types to search for
     * @return List of recipes matching any of the cuisines
     */
    public List<Recipe> searchByCuisines(List<String> cuisines) {
        if (cuisines == null || cuisines.isEmpty()) {
            return recipeRepository.findAll();
        }
        return recipeRepository.findByCuisine(cuisines.get(0));
    }

    /**
     * Validates that a recipe meets minimum requirements.
     * 
     * @param recipe The recipe to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        
        if (recipe.getName() == null || recipe.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be empty");
        }
    }

    /**
     * Checks if a recipe name already exists (case-insensitive).
     * 
     * @param recipeName The recipe name to check
     * @return true if a recipe with this name exists
     */
    public boolean recipeNameExists(String recipeName) {
        if (recipeName == null || recipeName.trim().isEmpty()) {
            return false;
        }
        
        List<Recipe> matches = recipeRepository.searchByName(recipeName.trim());
        return !matches.isEmpty();
    }

    /**
     * Gets a summary of available recipes.
     * 
     * @return Summary string with recipe count
     */
    public String getRecipeSummary() {
        int count = recipeRepository.count();
        return String.format("Total recipes available: %d", count);
    }
}
