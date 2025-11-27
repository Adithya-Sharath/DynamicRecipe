# MySQL Setup Guide

## Quick Start

### Prerequisites
- MySQL Server 8.0+ installed and running
- Java 11+ installed

### Setup Steps

**1. Configure MySQL Password**

Edit `src/main/java/com/recipeplanner/util/DbConnectionManager.java` (line 26):
```java
private static final String PASSWORD = "root";  // Change to your MySQL password
```

**2. Create Database**

Open MySQL command line:
```bash
mysql -u root -p
```

Run:
```sql
CREATE DATABASE dynamic_recipe;
USE dynamic_recipe;
SOURCE database_schema.sql;
EXIT;
```

Or run the SQL file directly:
```bash
mysql -u root -p < database_schema.sql
```

**3. Run Application**

Double-click `run_app.bat` or run:
```bash
.\run_app.bat
```

**Login Credentials:**
- Username: `demo` / Password: `demo123`
- Username: `admin` / Password: `admin123`

---

## What Happens on First Run

1. ✅ Connects to MySQL database
2. ✅ Creates demo users (in-memory)
3. ✅ Checks if database has recipes
4. ✅ If empty: Loads 5,938 recipes from CSV (~30 seconds)
5. ✅ If not empty: Uses existing data (instant)
6. ✅ Shows login screen

---

## Troubleshooting

### "Database connection failed"
- Verify MySQL is running: `net start MySQL80`
- Check username/password in `DbConnectionManager.java`
- Ensure database `dynamic_recipe` exists

### "Unknown database 'dynamic_recipe'"
```sql
CREATE DATABASE dynamic_recipe;
```

### "Access denied for user 'root'"
- Check password in `DbConnectionManager.java` matches your MySQL password

### "0 recipes loaded"
- Wait for CSV loading to complete (~30 seconds)
- Check that `Cleaned_Indian_Food_Dataset.csv` exists in project root

---

## Database Schema

**Table: recipes**
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `name` VARCHAR(500) NOT NULL
- `description` TEXT
- `cuisine` VARCHAR(100)
- `total_time_mins` INT DEFAULT 30
- `created_by` INT DEFAULT 0 (0 = system recipe, >0 = user ID)
- `source_url` VARCHAR(1000)
- `image_url` VARCHAR(1000)
- `raw_ingredients` TEXT
- `instructions` TEXT
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

**Indexes:**
- `idx_recipe_name` on `name`
- `idx_cuisine` on `cuisine`
- `idx_created_by` on `created_by`
- `idx_time` on `total_time_mins`

---

## Verification

Check database status:
```sql
mysql -u root -p

USE dynamic_recipe;
SHOW TABLES;
SELECT COUNT(*) FROM recipes;  -- Should show 5938
```

---

## Technical Details

### Connection Settings
- **Database**: `dynamic_recipe`
- **Host**: `localhost`
- **Port**: `3306`
- **User**: `root` (configurable)
- **Password**: Set in `DbConnectionManager.java`

### Files Modified for MySQL Integration
1. **DbConnectionManager.java** - Database connection manager
2. **RecipeRepository.java** - MySQL JDBC implementation
3. **InMemoryDataSeeder.java** - Auto-loads CSV on first run
4. **SimpleSwingApp.java** - Uses MySQL-aware initialization

### Files Unchanged
- ✅ All model classes (Recipe, User, Ingredient, etc.)
- ✅ All service classes (RecipeService, AuthenticationService, etc.)
- ✅ All UI components
- ✅ Business logic

---

## Features

### Persistent Storage
- Recipes saved permanently in MySQL
- Data survives application restarts
- No data loss

### Performance
- **First Run**: 30-60 seconds (CSV → MySQL)
- **Subsequent Runs**: <1 second startup
- **Search**: Fast (indexed queries)
- **Filter**: Optimized SQL queries

### My Recipes
- Add recipes to your personal collection
- View only your recipes with "My Recipes" button
- Remove recipes with "Remove from My Recipes" button
- Data persists in database

---

## Support

For issues:
1. Check console output for error messages
2. Verify MySQL is running
3. Confirm database and table exist
4. Ensure credentials are correct
5. Check CSV file is present

---

**Project ready to run with MySQL!** 🚀
