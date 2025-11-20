package com.recipeplanner.interfaces;

import java.util.List;

/**
 * Interface for searchable entities in the Recipe Planner application.
 * Demonstrates INTERFACE concept (Module 6).
 * 
 * Any class implementing this interface must provide search functionality
 * by defining how it appears in search results and what keywords it's indexed by.
 * 
 * @author Recipe Planner Team
 * @version 2.0
 */
public interface Searchable {
    
    /**
     * Returns a formatted string representation for search display.
     * This text is shown to users in search results.
     * 
     * @return Formatted string for display in search results
     */
    String getSearchDisplayText();
    
    /**
     * Returns a list of keywords that this entity should be searchable by.
     * Used for building search indices.
     * 
     * @return List of search keywords
     */
    List<String> getSearchKeywords();
}
