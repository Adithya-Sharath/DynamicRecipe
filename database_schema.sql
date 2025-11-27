-- ====================================================================
-- Recipe & Meal Planner - MySQL Database Schema
-- Version: 3.0 (MySQL Integration)
-- ====================================================================

-- Create database
CREATE DATABASE IF NOT EXISTS dynamic_recipe;
USE dynamic_recipe;

-- ====================================================================
-- RECIPES TABLE
-- Stores all recipe data from CSV + user-created recipes
-- ====================================================================

CREATE TABLE IF NOT EXISTS recipes (
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    name                VARCHAR(500) NOT NULL,
    description         TEXT,
    cuisine             VARCHAR(100),
    total_time_mins     INT DEFAULT 30,
    created_by          INT DEFAULT 0,          -- 0 = system/CSV recipe, >0 = user ID
    source_url          VARCHAR(1000),
    image_url           VARCHAR(1000),
    raw_ingredients     TEXT,                   -- Full ingredient text with quantities
    instructions        TEXT,                   -- Newline-separated instruction steps
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- INDEXES for better search performance (Optional but recommended)
-- ====================================================================

-- Index for recipe name search
CREATE INDEX idx_recipe_name ON recipes(name(100));

-- Index for cuisine filtering
CREATE INDEX idx_cuisine ON recipes(cuisine);

-- Index for finding user recipes
CREATE INDEX idx_created_by ON recipes(created_by);

-- Index for time-based queries
CREATE INDEX idx_time ON recipes(total_time_mins);

-- ====================================================================
-- OPTIONAL: Users table (for future enhancement)
-- Currently users are stored in-memory, but you can migrate them later
-- ====================================================================

/*
CREATE TABLE IF NOT EXISTS users (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(100) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    user_type       VARCHAR(50) NOT NULL,       -- 'RegularUser' or 'AdminUser'
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login      TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Add foreign key constraint if using users table
ALTER TABLE recipes 
    ADD CONSTRAINT fk_recipe_creator 
    FOREIGN KEY (created_by) REFERENCES users(id) 
    ON DELETE SET NULL;
*/

-- ====================================================================
-- VERIFICATION QUERIES
-- Run these to check if setup was successful
-- ====================================================================

-- Check if table was created
SHOW TABLES;

-- Check table structure
DESCRIBE recipes;

-- Check if indexes were created
SHOW INDEXES FROM recipes;

-- Count recipes (should be 0 initially, then 5938 after first app run)
SELECT COUNT(*) as recipe_count FROM recipes;

-- ====================================================================
-- SAMPLE QUERIES (for testing after data is loaded)
-- ====================================================================

-- Get all recipes
-- SELECT * FROM recipes LIMIT 10;

-- Search recipes by name
-- SELECT id, name, cuisine, total_time_mins FROM recipes WHERE name LIKE '%paneer%';

-- Get recipes by cuisine
-- SELECT COUNT(*) as count, cuisine FROM recipes GROUP BY cuisine ORDER BY count DESC;

-- Get quick recipes (under 30 minutes)
-- SELECT name, total_time_mins FROM recipes WHERE total_time_mins <= 30 ORDER BY total_time_mins;

-- ====================================================================
-- END OF SCHEMA
-- ====================================================================
