package com.recipeplanner.repository;

import com.recipeplanner.model.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository for Recipe entities using ArrayList.
 * Demonstrates Collections Framework, String handling, and search algorithms.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (In-Memory)
 */
public class RecipeRepository {
    
    // Demonstrates Collections Framework - ArrayList usage (Module 4)
    private final List<Recipe> recipes;
    private int nextId;
    
    /**
     * Constructor initializes the in-memory storage.
     */
    public RecipeRepository() {
        this.recipes = new ArrayList<>();
        this.nextId = 1;
    }
    
    /**
     * Finds a recipe by ID.
     * 
     * @param id The recipe ID
     * @return Optional containing the Recipe if found
     */
    public Optional<Recipe> findById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Saves a new recipe or updates an existing one.
     * 
     * @param recipe The recipe to save
     * @return The saved recipe with ID assigned
     */
    public Recipe save(Recipe recipe) {
        if (recipe.getId() == 0) {
            recipe.setId(nextId++);
        }
        
        Optional<Recipe> existing = findById(recipe.getId());
        if (existing.isPresent()) {
            int index = recipes.indexOf(existing.get());
            recipes.set(index, recipe);
        } else {
            recipes.add(recipe);
        }
        
        return recipe;
    }
    
    /**
     * Deletes a recipe by ID.
     * 
     * @param recipeId The recipe ID to delete
     * @return true if deletion was successful
     */
    public boolean delete(int recipeId) {
        return recipes.removeIf(recipe -> recipe.getId() == recipeId);
    }
    
    /**
     * Gets all recipes.
     * 
     * @return List of all recipes
     */
    public List<Recipe> findAll() {
        return new ArrayList<>(recipes);
    }
    
    /**
     * Searches recipes by name (case-insensitive).
     * Demonstrates String handling and filtering (Module 3).
     * 
     * @param searchTerm The search term
     * @return List of matching recipes
     */
    public List<Recipe> searchByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String normalizedSearch = searchTerm.toLowerCase().trim();
        List<Recipe> results = new ArrayList<>();
        
        for (Recipe recipe : recipes) {
            if (recipe.getName() != null && 
                recipe.getName().toLowerCase().contains(normalizedSearch)) {
                results.add(recipe);
            }
        }
        
        return results;
    }
    
    /**
     * Finds recipes by cuisine type.
     * Demonstrates filtering with Collections.
     * 
     * @param cuisine The cuisine type
     * @return List of recipes matching the cuisine
     */
    public List<Recipe> findByCuisine(String cuisine) {
        if (cuisine == null) {
            return new ArrayList<>();
        }
        
        List<Recipe> results = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (cuisine.equalsIgnoreCase(recipe.getCuisine())) {
                results.add(recipe);
            }
        }
        return results;
    }
    
    /**
     * Finds recipes created by a specific user.
     * 
     * @param userId The user ID
     * @return List of recipes created by the user
     */
    public List<Recipe> findByCreatedBy(int userId) {
        List<Recipe> results = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCreatedBy() == userId) {
                results.add(recipe);
            }
        }
        return results;
    }
    
    /**
     * Gets a limited number of recipes for pagination.
     * Demonstrates array-like operations on ArrayList.
     * 
     * @param limit Maximum number of recipes to return
     * @param offset Starting position
     * @return Sublist of recipes
     */
    public List<Recipe> findWithLimit(int limit, int offset) {
        int start = Math.min(offset, recipes.size());
        int end = Math.min(offset + limit, recipes.size());
        
        if (start >= recipes.size()) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(recipes.subList(start, end));
    }
    
    /**
     * Gets the total count of recipes.
     * 
     * @return The number of recipes in the repository
     */
    public int count() {
        return recipes.size();
    }
    
    /**
     * Clears all recipes from the repository.
     */
    public void clear() {
        recipes.clear();
        nextId = 1;
    }
}
