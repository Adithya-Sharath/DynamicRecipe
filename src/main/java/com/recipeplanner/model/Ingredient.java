package com.recipeplanner.model;

import java.util.Objects;

/**
 * Represents a normalized ingredient from the Indian Food Dataset.
 * Maps to ingredients extracted from the Cleaned-Ingredients column.
 * 
 * This class demonstrates encapsulation and proper JavaBean conventions.
 * 
 * @author Recipe Planner Team
 * @version 1.0
 */
public class Ingredient {
    private int id;
    private String name;
    private String category;

    /**
     * Default constructor for Ingredient.
     */
    public Ingredient() {
    }

    /**
     * Constructor with name only.
     * 
     * @param name The ingredient name (normalized and lowercase)
     */
    public Ingredient(String name) {
        this.name = name;
    }

    /**
     * Full constructor with name and category.
     * 
     * @param name The ingredient name
     * @param category The ingredient category (e.g., Produce, Spices, Dairy)
     */
    public Ingredient(String name, String category) {
        this.name = name;
        this.category = category;
    }

    /**
     * Complete constructor including database ID.
     * 
     * @param id The database-generated ingredient ID
     * @param name The ingredient name
     * @param category The ingredient category
     */
    public Ingredient(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Checks if this ingredient has a category assigned.
     * 
     * @return true if category is not null and not empty
     */
    public boolean hasCategory() {
        return category != null && !category.trim().isEmpty();
    }

    /**
     * Returns the category or a default value if not set.
     * 
     * @return The category or "Uncategorized"
     */
    public String getCategoryOrDefault() {
        return hasCategory() ? category : "Uncategorized";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
