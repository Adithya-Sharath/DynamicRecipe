package com.recipeplanner.util;

import com.recipeplanner.model.Recipe;
import com.recipeplanner.repository.RecipeRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load recipes from CSV file.
 * Demonstrates file I/O, String handling, and ArrayList usage.
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public class CSVRecipeLoader {
    
    /**
     * Loads recipes from the CSV dataset file.
     * Demonstrates String parsing and Collections (Modules 3, 4).
     * 
     * @param recipeRepository The repository to store recipes
     * @return Number of recipes loaded
     */
    public static int loadRecipesFromCSV(RecipeRepository recipeRepository) {
        int count = 0;
        String csvFile = "Cleaned_Indian_Food_Dataset.csv";
        
        try {
            // Try to read from file system first
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            count = parseCSV(br, recipeRepository);
            br.close();
            
        } catch (IOException e) {
            System.err.println("Error loading recipes from CSV: " + e.getMessage());
        }
        
        return count;
    }
    
    /**
     * Parses CSV content and creates Recipe objects.
     * Demonstrates String manipulation and exception handling.
     * Handles multi-line CSV entries properly.
     */
    private static int parseCSV(BufferedReader br, RecipeRepository recipeRepository) throws IOException {
        int count = 0;
        String line;
        boolean firstLine = true;
        StringBuilder currentLine = new StringBuilder();
        boolean inQuotes = false;
        
        while ((line = br.readLine()) != null) {
            // Skip header line
            if (firstLine) {
                firstLine = false;
                continue;
            }
            
            // Handle multi-line CSV entries (fields with newlines inside quotes)
            currentLine.append(line);
            
            // Count quotes to see if we're inside a quoted field
            for (char c : line.toCharArray()) {
                if (c == '"') {
                    inQuotes = !inQuotes;
                }
            }
            
            // If we're still inside quotes, continue reading
            if (inQuotes) {
                currentLine.append("\n");
                continue;
            }
            
            // We have a complete record, parse it
            try {
                Recipe recipe = parseRecipeLine(currentLine.toString());
                if (recipe != null) {
                    recipeRepository.save(recipe);
                    count++;
                }
            } catch (Exception e) {
                // Skip malformed lines
                System.err.println("Error parsing line: " + e.getMessage());
            }
            
            // Reset for next record
            currentLine = new StringBuilder();
        }
        
        return count;
    }
    
    /**
     * Parses a single CSV line into a Recipe object.
     * Demonstrates String handling with split and trim (Module 3).
     * 
     * CSV Format:
     * 0: TranslatedRecipeName
     * 1: TranslatedIngredients
     * 2: TotalTimeInMins
     * 3: Cuisine
     * 4: TranslatedInstructions
     * 5: URL
     * 6: Cleaned-Ingredients
     * 7: image-url
     * 8: Ingredient-count
     */
    private static Recipe parseRecipeLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        // Split by comma, handling quoted fields
        String[] fields = splitCSVLine(line);
        
        if (fields.length < 5) {
            return null; // Not enough fields
        }
        
        try {
            // Parse fields according to CSV structure
            String name = cleanField(fields[0]);              // TranslatedRecipeName
            String ingredients = cleanField(fields[1]);       // TranslatedIngredients (with quantities!)
            int totalTime = parseTime(fields[2]);             // TotalTimeInMins
            String cuisine = cleanField(fields[3]);           // Cuisine
            String instructions = cleanField(fields[4]);      // TranslatedInstructions
            String imageUrl = cleanField(fields.length > 7 ? fields[7] : "");  // image-url
            
            // Validate that we have a proper recipe name
            if (name == null || name.trim().isEmpty() || name.length() < 2) {
                return null;
            }
            
            // Remove any newlines or extra whitespace from name
            name = name.replaceAll("\\n", " ").replaceAll("\\s+", " ").trim();
            
            // Always use TranslatedIngredients (column 1) to preserve quantities
            // DO NOT use Cleaned-Ingredients as it strips quantities
            
            // Create recipe
            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setCuisine(cuisine);
            recipe.setTotalTimeInMins(totalTime);
            recipe.setRawIngredientsText(ingredients);
            recipe.setCreatedBy(0); // System recipe
            recipe.setImageUrl(imageUrl);
            
            // Parse instructions - split by period or newline
            if (instructions != null && !instructions.isEmpty()) {
                // Split by newline first, then by period if no newlines
                String[] steps = instructions.split("\\n|\\.");
                for (String step : steps) {
                    String trimmedStep = step.trim();
                    if (!trimmedStep.isEmpty() && trimmedStep.length() > 5) {
                        recipe.addInstruction(trimmedStep);
                    }
                }
            }
            
            return recipe;
            
        } catch (Exception e) {
            System.err.println("Error parsing recipe: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Splits a CSV line handling quoted fields.
     * Demonstrates String handling (Module 3).
     */
    private static String[] splitCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
    
    /**
     * Cleans a CSV field by removing quotes and trimming.
     */
    private static String cleanField(String field) {
        if (field == null) {
            return "";
        }
        
        field = field.trim();
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        
        return field.trim();
    }
    
    /**
     * Parses time field from CSV.
     */
    private static int parseTime(String timeStr) {
        try {
            String cleaned = cleanField(timeStr);
            if (cleaned.isEmpty()) {
                return 30; // Default time
            }
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 30; // Default time
        }
    }
}
