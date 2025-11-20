package com.recipeplanner.repository;

import com.recipeplanner.model.Ingredient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository for Ingredient entities using ArrayList.
 * Demonstrates Collections Framework and data management.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (In-Memory)
 */
public class IngredientRepository {
    
    // Demonstrates Collections Framework - ArrayList usage (Module 4)
    private final List<Ingredient> ingredients;
    private int nextId;
    
    /**
     * Constructor initializes the in-memory storage.
     */
    public IngredientRepository() {
        this.ingredients = new ArrayList<>();
        this.nextId = 1;
    }
    
    /**
     * Finds an ingredient by ID.
     * 
     * @param id The ingredient ID
     * @return Optional containing the Ingredient if found
     */
    public Optional<Ingredient> findById(int id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) {
                return Optional.of(ingredient);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Finds an ingredient by name (case-insensitive).
     * Demonstrates String handling (Module 3).
     * 
     * @param name The ingredient name
     * @return Optional containing the Ingredient if found
     */
    public Optional<Ingredient> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        
        String normalizedName = name.trim().toLowerCase();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName() != null && 
                ingredient.getName().toLowerCase().equals(normalizedName)) {
                return Optional.of(ingredient);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Saves a new ingredient or updates an existing one.
     * 
     * @param ingredient The ingredient to save
     * @return The saved ingredient with ID assigned
     */
    public Ingredient save(Ingredient ingredient) {
        if (ingredient.getId() == 0) {
            ingredient.setId(nextId++);
        }
        
        Optional<Ingredient> existing = findById(ingredient.getId());
        if (existing.isPresent()) {
            int index = ingredients.indexOf(existing.get());
            ingredients.set(index, ingredient);
        } else {
            ingredients.add(ingredient);
        }
        
        return ingredient;
    }
    
    /**
     * Finds or creates an ingredient by name.
     * If the ingredient doesn't exist, creates it.
     * 
     * @param name The ingredient name
     * @return The existing or newly created ingredient
     */
    public Ingredient findOrCreate(String name) {
        Optional<Ingredient> existing = findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        Ingredient newIngredient = new Ingredient(name);
        return save(newIngredient);
    }
    
    /**
     * Finds ingredients by category.
     * 
     * @param category The category name
     * @return List of ingredients in that category
     */
    public List<Ingredient> findByCategory(String category) {
        List<Ingredient> results = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (category != null && category.equalsIgnoreCase(ingredient.getCategory())) {
                results.add(ingredient);
            }
        }
        return results;
    }
    
    /**
     * Gets all ingredients.
     * 
     * @return List of all ingredients
     */
    public List<Ingredient> findAll() {
        return new ArrayList<>(ingredients);
    }
    
    /**
     * Deletes an ingredient by ID.
     * 
     * @param ingredientId The ingredient ID
     * @return true if deletion was successful
     */
    public boolean delete(int ingredientId) {
        return ingredients.removeIf(ing -> ing.getId() == ingredientId);
    }
    
    /**
     * Gets the total count of ingredients.
     * 
     * @return The number of ingredients
     */
    public int count() {
        return ingredients.size();
    }
    
    /**
     * Clears all ingredients from the repository.
     */
    public void clear() {
        ingredients.clear();
        nextId = 1;
    }
}
