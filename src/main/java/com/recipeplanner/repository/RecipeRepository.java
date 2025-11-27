package com.recipeplanner.repository;

import com.recipeplanner.model.Recipe;
import com.recipeplanner.util.DbConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MySQL-backed repository for Recipe entities using JDBC.
 * Maintains the same interface as in-memory version for seamless migration.
 * Demonstrates database integration while preserving OOP principles.
 * 
 * @author Recipe Planner Team
 * @version 3.0 (MySQL Integration)
 */
public class RecipeRepository {
    
    /**
     * Constructor - no initialization needed for database version.
     */
    public RecipeRepository() {
        // Connection is obtained per-operation via DbConnectionManager
    }
    
    /**
     * Finds a recipe by ID.
     * 
     * @param id The recipe ID
     * @return Optional containing the Recipe if found
     */
    public Optional<Recipe> findById(int id) {
        String sql = "SELECT * FROM recipes WHERE id = ?";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToRecipe(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding recipe by ID: " + e.getMessage());
            e.printStackTrace();
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
            return insertRecipe(recipe);
        } else {
            return updateRecipe(recipe);
        }
    }
    
    /**
     * Inserts a new recipe into the database.
     */
    private Recipe insertRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (name, description, cuisine, total_time_mins, " +
                     "created_by, source_url, image_url, raw_ingredients, instructions) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setRecipeParameters(ps, recipe);
            ps.executeUpdate();
            
            // Get auto-generated ID
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    recipe.setId(keys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting recipe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipe;
    }
    
    /**
     * Updates an existing recipe in the database.
     */
    private Recipe updateRecipe(Recipe recipe) {
        String sql = "UPDATE recipes SET name = ?, description = ?, cuisine = ?, " +
                     "total_time_mins = ?, created_by = ?, source_url = ?, " +
                     "image_url = ?, raw_ingredients = ?, instructions = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            setRecipeParameters(ps, recipe);
            ps.setInt(10, recipe.getId());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating recipe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipe;
    }
    
    /**
     * Helper method to set recipe parameters in PreparedStatement.
     */
    private void setRecipeParameters(PreparedStatement ps, Recipe recipe) throws SQLException {
        ps.setString(1, recipe.getName());
        ps.setString(2, recipe.getDescription());
        ps.setString(3, recipe.getCuisine());
        ps.setInt(4, recipe.getTotalTimeInMins());
        ps.setInt(5, recipe.getCreatedBy());
        ps.setString(6, recipe.getSourceUrl());
        ps.setString(7, recipe.getImageUrl());
        ps.setString(8, recipe.getRawIngredientsText());
        
        // Serialize instructions list to string (newline-separated)
        String instructionsText = recipe.getInstructions() != null 
            ? String.join("\n", recipe.getInstructions()) 
            : "";
        ps.setString(9, instructionsText);
    }
    
    /**
     * Deletes a recipe by ID.
     * 
     * @param recipeId The recipe ID to delete
     * @return true if deletion was successful
     */
    public boolean delete(int recipeId) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, recipeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting recipe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets all recipes.
     * 
     * @return List of all recipes
     */
    public List<Recipe> findAll() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes ORDER BY name";
        
        try (Connection conn = DbConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                recipes.add(mapRowToRecipe(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all recipes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Searches recipes by name (case-insensitive).
     * Also searches in ingredients text.
     * 
     * @param searchTerm The search term
     * @return List of matching recipes
     */
    public List<Recipe> searchByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE name LIKE ? OR raw_ingredients LIKE ? ORDER BY name";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String pattern = "%" + searchTerm.trim() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRowToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching recipes by name: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Finds recipes by cuisine type.
     * 
     * @param cuisine The cuisine type
     * @return List of recipes matching the cuisine
     */
    public List<Recipe> findByCuisine(String cuisine) {
        if (cuisine == null) {
            return new ArrayList<>();
        }
        
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE cuisine = ? ORDER BY name";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cuisine);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRowToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding recipes by cuisine: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Finds recipes created by a specific user.
     * 
     * @param userId The user ID
     * @return List of recipes created by the user
     */
    public List<Recipe> findByCreatedBy(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE created_by = ? ORDER BY name";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRowToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding recipes by creator: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Gets a limited number of recipes for pagination.
     * 
     * @param limit Maximum number of recipes to return
     * @param offset Starting position
     * @return Sublist of recipes
     */
    public List<Recipe> findWithLimit(int limit, int offset) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes ORDER BY name LIMIT ? OFFSET ?";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapRowToRecipe(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding recipes with limit: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Gets the total count of recipes.
     * 
     * @return The number of recipes in the repository
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM recipes";
        
        try (Connection conn = DbConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting recipes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Clears all recipes from the database.
     * WARNING: This deletes all data!
     */
    public void clear() {
        String sql = "DELETE FROM recipes";
        
        try (Connection conn = DbConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("All recipes cleared from database");
            
        } catch (SQLException e) {
            System.err.println("Error clearing recipes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Maps a database row to a Recipe object.
     * Demonstrates ResultSet handling and object construction.
     */
    private Recipe mapRowToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        
        recipe.setId(rs.getInt("id"));
        recipe.setName(rs.getString("name"));
        recipe.setDescription(rs.getString("description"));
        recipe.setCuisine(rs.getString("cuisine"));
        recipe.setTotalTimeInMins(rs.getInt("total_time_mins"));
        recipe.setCreatedBy(rs.getInt("created_by"));
        recipe.setSourceUrl(rs.getString("source_url"));
        recipe.setImageUrl(rs.getString("image_url"));
        recipe.setRawIngredientsText(rs.getString("raw_ingredients"));
        
        // Deserialize instructions (newline-separated string back to List)
        String instructionsText = rs.getString("instructions");
        if (instructionsText != null && !instructionsText.trim().isEmpty()) {
            List<String> instructions = Arrays.stream(instructionsText.split("\n"))
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
            recipe.setInstructions(instructions);
        } else {
            recipe.setInstructions(new ArrayList<>());
        }
        
        return recipe;
    }
}
