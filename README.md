# DynamicRecipe - Recipe & Meal Planner

A modern Java Swing application for browsing, managing, and organizing Indian recipes with MySQL database integration.

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Swing](https://img.shields.io/badge/GUI-Swing-blue.svg)
![MySQL](https://img.shields.io/badge/Storage-MySQL-blue.svg)
![Recipes](https://img.shields.io/badge/Recipes-5938+-brightgreen.svg)

---

## Features

- **5,938+ Indian Recipes** - Comprehensive database of authentic Indian dishes
- **Modern UI Design** - Clean mint green theme with rounded corners and custom Lexend font
- **User Authentication** - Login system with regular and admin users
- **Recipe Management** - Add recipes to personal collection, search, and sort by cooking time
- **Shopping List** - Add ingredients from multiple recipes to a grocery list
- **MySQL Integration** - Persistent storage for recipes and user data

---

## Quick Start

### Prerequisites
- Java JDK 17 or higher
- MySQL Server 8.0+ installed and running

### Setup

1. **Create MySQL Database**
   ```sql
   CREATE DATABASE recipe_planner;
   USE recipe_planner;
   ```

2. **Run the schema**
   ```bash
   mysql -u root -p recipe_planner < database_schema.sql
   ```

3. **Configure database password** in `src/main/java/com/recipeplanner/util/DbConnectionManager.java`:
   ```java
   private static final String PASSWORD = "your_password";
   ```

4. **Run the application**
   ```bash
   run_app.bat
   ```

### Login Credentials

| User Type | Username | Password |
|-----------|----------|----------|
| Regular   | demo     | demo123  |
| Admin     | admin    | admin123 |

---

## Project Structure

```
OOPS_Project/
├── src/main/java/com/recipeplanner/
│   ├── SimpleSwingApp.java              # Main GUI application
│   │
│   ├── model/                           # Data models
│   │   ├── User.java                    # Abstract user class
│   │   ├── AdminUser.java               # Admin (extends User)
│   │   ├── RegularUser.java             # Regular user (extends User)
│   │   ├── Recipe.java                  # Recipe with Builder pattern
│   │   ├── Ingredient.java              # Ingredient model
│   │   ├── Measurement.java             # Measurement units
│   │   └── MealType.java                # Enum for meal types
│   │
│   ├── repository/                      # Data access layer
│   │   ├── UserRepository.java          # User CRUD operations
│   │   ├── RecipeRepository.java        # Recipe CRUD (MySQL)
│   │   ├── IngredientRepository.java    # Ingredient storage
│   │   └── RepositoryManager.java       # Singleton factory
│   │
│   ├── service/                         # Business logic
│   │   ├── AuthenticationService.java   # Login/logout
│   │   └── RecipeService.java           # Recipe operations
│   │
│   ├── util/                            # Utilities
│   │   ├── DbConnectionManager.java     # MySQL connection pool
│   │   ├── CSVRecipeLoader.java         # CSV data importer
│   │   ├── InMemoryDataSeeder.java      # Initial data setup
│   │   └── PasswordHasher.java          # SHA-256 hashing
│   │
│   ├── interfaces/
│   │   └── Searchable.java              # Search interface
│   │
│   └── exceptions/
│       └── AuthenticationException.java # Custom exception
│
├── src/main/resources/
│   ├── dataset/
│   │   └── Cleaned_Indian_Food_Dataset.csv  # Recipe data
│   └── fonts/
│       └── Lexend-Bold.ttf              # Custom UI font
│
├── database_schema.sql                  # MySQL table definitions
├── pom.xml                              # Maven configuration
├── run_app.bat                          # Quick start script
└── README.md
```

---

## OOP Concepts Demonstrated

### 1. Inheritance
```
User (abstract)
  ├── AdminUser
  └── RegularUser
```
- Abstract class with common fields (id, username, password)
- Subclasses override `getUserType()` method

### 2. Encapsulation
- Private fields with public getters/setters
- Data hiding in all model classes

### 3. Abstraction
- `Searchable` interface defines search contract
- Abstract `User` class provides common behavior

### 4. Polymorphism
- Runtime method dispatch via `User.getUserType()`
- Interface implementation in `Recipe`

### 5. Builder Pattern
```java
Recipe recipe = new Recipe.RecipeBuilder("Paneer Butter Masala")
    .withCuisine("North Indian")
    .withCookingTime(45)
    .addInstruction("Heat ghee in a pan")
    .addInstruction("Add spices and cream")
    .build();
```

### 6. Inner Classes
- `Recipe.RecipeBuilder` - Static inner class for fluent construction
- `RoundedBorder` - Custom Swing border component

### 7. Exception Handling
- Custom `AuthenticationException` for login errors
- Try-catch blocks for database and file operations

### 8. Collections Framework
- `ArrayList<Recipe>` - Recipe storage
- `LinkedHashMap<String, List<String>>` - Shopping list (preserves order)
- Stream API for filtering and sorting

---

## UI Design

### Theme
- **Background**: Mint Green `#E8F5E9`
- **Accent**: Dark Green `#2E7D32`
- **Font**: Lexend Bold (custom TTF)
- **Border Radius**: 15px cards, 12px buttons

### Components
- **Recipe Cards** - Single column layout with rounded corners
- **Search Bar** - Filter recipes by name
- **Buttons** - View my recipes, View ingredient list, Sort by time
- **Dialogs** - Undecorated with custom close button

---

## Database Schema

```sql
CREATE TABLE recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    cuisine VARCHAR(100),
    total_time_mins INT,
    description TEXT,
    raw_ingredients_text TEXT,
    instructions TEXT,
    created_by INT DEFAULT 0
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(20) DEFAULT 'REGULAR'
);
```

---

## How to Run

### Option 1: Batch File
```bash
run_app.bat
```

### Option 2: Command Line
```bash
# Compile
javac -d target/classes -cp "target/classes;path/to/mysql-connector-j-8.0.33.jar" src/main/java/com/recipeplanner/SimpleSwingApp.java

# Run
java -cp "target/classes;path/to/mysql-connector-j-8.0.33.jar" com.recipeplanner.SimpleSwingApp
```

### Option 3: Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.recipeplanner.SimpleSwingApp"
```

---

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Java JDK   | 17      | Runtime |
| MySQL      | 8.0+    | Database |
| MySQL Connector/J | 8.0.33 | JDBC Driver |
| OpenCSV    | 5.7.1   | CSV parsing |

