package com.recipeplanner.model;

import com.recipeplanner.interfaces.Searchable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a recipe in the Recipe & Meal Planner application.
 * Implements Searchable interface to demonstrate INTERFACE usage (Module 6).
 * Contains an inner class RecipeBuilder to demonstrate INNER CLASSES (Module 6).
 * 
 * Fields map to dataset columns:
 * - name: TranslatedRecipeName
 * - cuisine: Cuisine
 * - totalTimeInMins: TotalTimeInMins
 * - sourceUrl: URL
 * - imageUrl: image-url
 * - rawIngredientsText: Cleaned-Ingredients (for display)
 * - instructions: TranslatedInstructions (parsed into steps)
 * - ingredients: Structured list of IngredientQuantity objects
 * 
 * Supports both:
 * - Recipes imported from the dataset (may have null amounts/units)
 * - User-created recipes with full structured ingredient quantities
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Refactored for in-memory storage)
 */
public class Recipe implements Searchable {
    private int id;
    private String name;
    private String description;
    private String cuisine;
    private int totalTimeInMins;
    private int createdBy;  // User ID who created this recipe (0 for seeded recipes)
    private String sourceUrl;
    private String imageUrl;
    private String rawIngredientsText;  // Original ingredient text from dataset
    private List<String> instructions;
    // Note: For simplicity, using rawIngredientsText instead of structured IngredientQuantity list

    /**
     * Default constructor for Recipe.
     */
    public Recipe() {
        this.instructions = new ArrayList<>();
    }

    /**
     * Constructor with basic required fields.
     * 
     * @param name The recipe name
     */
    public Recipe(String name) {
        this();
        this.name = name;
    }

    /**
     * Full constructor for dataset-imported recipes.
     * 
     * @param id Recipe ID
     * @param name Recipe name (TranslatedRecipeName)
     * @param description Short description (optional)
     * @param cuisine Cuisine type (Cuisine column)
     * @param totalTimeInMins Total cooking time (TotalTimeInMins)
     * @param createdBy User ID (0 for seeded recipes)
     * @param sourceUrl Original recipe URL (URL column)
     * @param imageUrl Recipe image URL (image-url column)
     * @param rawIngredientsText Raw ingredients text (Cleaned-Ingredients)
     */
    public Recipe(int id, String name, String description, String cuisine, 
                  int totalTimeInMins, int createdBy, String sourceUrl, 
                  String imageUrl, String rawIngredientsText) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisine = cuisine;
        this.totalTimeInMins = totalTimeInMins;
        this.createdBy = createdBy;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.rawIngredientsText = rawIngredientsText;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getTotalTimeInMins() {
        return totalTimeInMins;
    }

    public void setTotalTimeInMins(int totalTimeInMins) {
        this.totalTimeInMins = totalTimeInMins;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRawIngredientsText() {
        return rawIngredientsText;
    }

    public void setRawIngredientsText(String rawIngredientsText) {
        this.rawIngredientsText = rawIngredientsText;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions != null ? instructions : new ArrayList<>();
    }

    public List<IngredientQuantity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientQuantity> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    // Business Methods

    /**
     * Adds a single instruction step to the recipe.
     * 
     * @param instruction The instruction text to add
     */
    public void addInstruction(String instruction) {
        if (instruction != null && !instruction.trim().isEmpty()) {
            this.instructions.add(instruction.trim());
        }
    }

    /**
     * Adds an ingredient with quantity to the recipe.
     * 
     * @param ingredientQuantity The ingredient with measurement
     */
    public void addIngredient(IngredientQuantity ingredientQuantity) {
        if (ingredientQuantity != null) {
            this.ingredients.add(ingredientQuantity);
        }
    }

    /**
     * Adds an ingredient without specific quantity (for dataset recipes).
     * 
     * @param ingredient The ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            this.ingredients.add(new IngredientQuantity(ingredient));
        }
    }

    /**
     * Checks if the recipe has all required fields to be considered complete.
     * 
     * @return true if recipe has name, at least one instruction, and at least one ingredient
     */
    public boolean isComplete() {
        return name != null && !name.trim().isEmpty() &&
               !instructions.isEmpty() &&
               !ingredients.isEmpty();
    }

    /**
     * Gets the count of ingredients in this recipe.
     * 
     * @return The number of ingredients
     */
    public int getIngredientCount() {
        return ingredients.size();
    }

    /**
     * Gets the count of instruction steps.
     * 
     * @return The number of instruction steps
     */
    public int getInstructionCount() {
        return instructions.size();
    }

    /**
     * Checks if this recipe has an image URL.
     * 
     * @return true if imageUrl is not null and not empty
     */
    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    /**
     * Checks if this recipe was imported from the dataset (not user-created).
     * 
     * @return true if createdBy is 0
     */
    public boolean isSeededRecipe() {
        return createdBy == 0;
    }

    /**
     * Gets a formatted time string for display.
     * Example: "45 mins" or "1 hr 30 mins"
     * 
     * @return Formatted time string
     */
    public String getFormattedTime() {
        if (totalTimeInMins <= 0) {
            return "Time not specified";
        } else if (totalTimeInMins < 60) {
            return totalTimeInMins + " mins";
        } else {
            int hours = totalTimeInMins / 60;
            int mins = totalTimeInMins % 60;
            if (mins == 0) {
                return hours + " hr" + (hours > 1 ? "s" : "");
            } else {
                return hours + " hr " + mins + " mins";
            }
        }
    }

    /**
     * Gets a summary of the recipe for display.
     * 
     * @return Summary string with key recipe information
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (cuisine != null && !cuisine.trim().isEmpty()) {
            sb.append(" (").append(cuisine).append(")");
        }
        sb.append(" - ").append(getFormattedTime());
        sb.append(" - ").append(ingredients.size()).append(" ingredients");
        return sb.toString();
    }

    /**
     * Gets an unmodifiable view of the ingredients list.
     * 
     * @return Unmodifiable list of ingredients
     */
    public List<IngredientQuantity> getIngredientsReadOnly() {
        return Collections.unmodifiableList(ingredients);
    }

    /**
     * Gets an unmodifiable view of the instructions list.
     * 
     * @return Unmodifiable list of instructions
     */
    public List<String> getInstructionsReadOnly() {
        return Collections.unmodifiableList(instructions);
    }
    
    // Searchable interface implementation (Module 6)
    
    /**
     * Checks if this recipe matches the given search term.
     * Searches in recipe name, cuisine, and ingredients.
     * Demonstrates INTERFACE implementation (Module 6).
     * 
     * @param searchTerm The term to search for
     * @return true if the recipe matches the search term
     */
    @Override
    public boolean matches(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return false;
        }
        
        String lowerSearch = searchTerm.toLowerCase().trim();
        
        // Search in name
        if (name != null && name.toLowerCase().contains(lowerSearch)) {
            return true;
        }
        
        // Search in cuisine
        if (cuisine != null && cuisine.toLowerCase().contains(lowerSearch)) {
            return true;
        }
        
        // Search in raw ingredients text
        if (rawIngredientsText != null && rawIngredientsText.toLowerCase().contains(lowerSearch)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a searchable display string for this recipe.
     * Demonstrates INTERFACE implementation (Module 6).
     * 
     * @return A formatted string for display in search results
     */
    @Override
    public String getSearchDisplayText() {
        return String.format("%s (%s) - %s - %d ingredients", 
            name, cuisine != null ? cuisine : "Unknown", 
            getFormattedTime(), ingredients.size());
    }
    
    /**
     * Gets the relevance score for a given search term.
     * Demonstrates INTERFACE implementation (Module 6).
     * 
     * @param searchTerm The search term
     * @return Relevance score (0-100)
     */
    @Override
    public int getRelevanceScore(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return 0;
        }
        
        String lowerSearch = searchTerm.toLowerCase().trim();
        int score = 0;
        
        // Exact name match gets highest score
        if (name != null && name.toLowerCase().equals(lowerSearch)) {
            score += 100;
        } else if (name != null && name.toLowerCase().contains(lowerSearch)) {
            score += 50;
        }
        
        // Cuisine match
        if (cuisine != null && cuisine.toLowerCase().equals(lowerSearch)) {
            score += 30;
        } else if (cuisine != null && cuisine.toLowerCase().contains(lowerSearch)) {
            score += 15;
        }
        
        // Ingredient match
        if (rawIngredientsText != null && rawIngredientsText.toLowerCase().contains(lowerSearch)) {
            score += 20;
        }
        
        return Math.min(score, 100);
    }
    
    // Inner class demonstration (Module 6)
    
    /**
     * Builder pattern inner class for constructing Recipe objects.
     * Demonstrates INNER CLASS usage (Module 6).
     * 
     * This builder allows for fluent and flexible recipe construction,
     * especially useful when not all fields are required.
     */
    public static class RecipeBuilder {
        private final Recipe recipe;
        
        /**
         * Constructor initializes a new Recipe instance.
         * 
         * @param name The recipe name (required)
         */
        public RecipeBuilder(String name) {
            this.recipe = new Recipe(name);
        }
        
        /**
         * Sets the description.
         * 
         * @param description The description
         * @return This builder for method chaining
         */
        public RecipeBuilder withDescription(String description) {
            recipe.setDescription(description);
            return this;
        }
        
        /**
         * Sets the cuisine type.
         * 
         * @param cuisine The cuisine type
         * @return This builder for method chaining
         */
        public RecipeBuilder withCuisine(String cuisine) {
            recipe.setCuisine(cuisine);
            return this;
        }
        
        /**
         * Sets the cooking time.
         * 
         * @param minutes Total time in minutes
         * @return This builder for method chaining
         */
        public RecipeBuilder withCookingTime(int minutes) {
            recipe.setTotalTimeInMins(minutes);
            return this;
        }
        
        /**
         * Sets the creator user ID.
         * 
         * @param userId The user ID
         * @return This builder for method chaining
         */
        public RecipeBuilder createdBy(int userId) {
            recipe.setCreatedBy(userId);
            return this;
        }
        
        /**
         * Adds an instruction step.
         * 
         * @param instruction The instruction text
         * @return This builder for method chaining
         */
        public RecipeBuilder addInstruction(String instruction) {
            recipe.addInstruction(instruction);
            return this;
        }
        
        /**
         * Adds an ingredient.
         * 
         * @param ingredient The ingredient to add
         * @return This builder for method chaining
         */
        public RecipeBuilder addIngredient(IngredientQuantity ingredient) {
            recipe.addIngredient(ingredient);
            return this;
        }
        
        /**
         * Builds and returns the Recipe object.
         * 
         * @return The constructed Recipe
         */
        public Recipe build() {
            return recipe;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id && Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", totalTimeInMins=" + totalTimeInMins +
                ", ingredientCount=" + ingredients.size() +
                ", instructionCount=" + instructions.size() +
                '}';
    }
}
