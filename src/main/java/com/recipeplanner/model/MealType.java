package com.recipeplanner.model;

/**
 * Enumeration representing the types of meals in a daily meal plan.
 * Demonstrates enum usage with fields and methods.
 * 
 * This enum is used to categorize meal slots in the weekly meal planner.
 * 
 * @author Recipe Planner Team
 * @version 1.0
 */
public enum MealType {
    /**
     * Morning meal, typically 7-10 AM
     */
    BREAKFAST("Breakfast"),
    
    /**
     * Midday meal, typically 12-2 PM
     */
    LUNCH("Lunch"),
    
    /**
     * Evening meal, typically 7-9 PM
     */
    DINNER("Dinner");

    private final String displayName;

    /**
     * Constructor for MealType enum.
     * 
     * @param displayName The human-readable name for display in UI
     */
    MealType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name for this meal type.
     * 
     * @return The human-readable meal type name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string to a MealType enum value.
     * Case-insensitive matching.
     * 
     * @param text The string to convert
     * @return The corresponding MealType, or null if not found
     */
    public static MealType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        for (MealType type : MealType.values()) {
            if (type.name().equalsIgnoreCase(text) || 
                type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets the typical time range for this meal type.
     * 
     * @return A string describing the typical time range
     */
    public String getTypicalTimeRange() {
        switch (this) {
            case BREAKFAST:
                return "7:00 AM - 10:00 AM";
            case LUNCH:
                return "12:00 PM - 2:00 PM";
            case DINNER:
                return "7:00 PM - 9:00 PM";
            default:
                return "N/A";
        }
    }

    /**
     * Gets an icon or emoji representation for this meal type.
     * Useful for UI display.
     * 
     * @return An emoji or symbol representing this meal type
     */
    public String getIcon() {
        switch (this) {
            case BREAKFAST:
                return "☕";
            case LUNCH:
                return "🍽️";
            case DINNER:
                return "🌙";
            default:
                return "🍴";
        }
    }

    /**
     * Returns a formatted string with icon and display name.
     * 
     * @return Formatted string for UI display
     */
    public String toDisplayString() {
        return getIcon() + " " + displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
