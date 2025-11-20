# Recipe & Meal Planner - Java OOP Project Documentation

## Project Overview
A comprehensive Recipe and Meal Planning application built with Java Swing GUI, demonstrating all core Object-Oriented Programming concepts and Java programming modules required for college curriculum.

**Author:** OOPS Project Team  
**Version:** 2.0 (In-Memory Implementation)  
**Technology:** Java SE 11+, Swing GUI  
**Data Storage:** In-Memory (ArrayList, HashMap)  

**Current Status:** ✅ **READY TO RUN**  
The application is fully functional using pre-compiled classes in `target/classes`. Simply run the command below to start the application.

```bash
java -cp target/classes com.recipeplanner.SimpleSwingApp
```  

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [How to Run](#how-to-run)
3. [Module Coverage](#module-coverage)
4. [Core Concepts Demonstrated](#core-concepts-demonstrated)
5. [Class Descriptions](#class-descriptions)
6. [Design Patterns Used](#design-patterns-used)

---

## Project Structure

```
OOPS_Project/
├── src/main/java/com/recipeplanner/
│   ├── model/                      # Entity classes (Module 1, 2)
│   │   ├── User.java              # Base class for polymorphism
│   │   ├── RegularUser.java       # Inherits from User
│   │   ├── AdminUser.java         # Inherits from User  
│   │   ├── Recipe.java            # Implements Searchable interface
│   │   ├── Ingredient.java        # Basic encapsulation
│   │   ├── MealType.java          # Enum demonstration
│   │   └── Measurement.java       # Value object
│   │
│   ├── repository/                 # Data access layer (Module 4)
│   │   ├── UserRepository.java    # ArrayList-based storage
│   │   ├── RecipeRepository.java  # ArrayList-based storage
│   │   ├── IngredientRepository.java
│   │   └── RepositoryManager.java # Singleton pattern
│   │
│   ├── service/                    # Business logic (Module 7)
│   │   ├── AuthenticationService.java  # Exception handling
│   │   └── RecipeService.java          # Collections usage
│   │
│   ├── util/                       # Utility classes (Module 3, 7)
│   │   ├── PasswordHasher.java         # String handling, security
│   │   ├── InMemoryDataSeeder.java     # Array usage, data initialization
│   │   └── CSVRecipeLoader.java        # File I/O, String parsing
│   │
│   ├── exceptions/                 # Custom exceptions (Module 7)
│   │   └── AuthenticationException.java
│   │
│   ├── interfaces/                 # Interface definitions (Module 6)
│   │   └── Searchable.java
│   │
│   ├── SimpleSwingApp.java        # Main Swing UI application
│   └── SimpleConsoleApp.java      # Console-based demo
│
├── Cleaned_Indian_Food_Dataset.csv  # Recipe data (5,938 recipes)
├── pom.xml                          # Maven configuration
└── PROJECT_DOCUMENTATION.md         # This file

```

---

## How to Run

### Prerequisites
- Java JDK 11 or higher installed
- Ensure `JAVA_HOME` environment variable is set

### Running the Swing GUI Application

**IMPORTANT:** The project is ready to run using pre-compiled classes in `target/classes`.

**Run the application:**
```bash
cd OOPS_Project
java -cp target/classes com.recipeplanner.SimpleSwingApp
```

**On Windows PowerShell:**
```powershell
cd C:\Users\shara\OneDrive\Desktop\Work\College\OOPS_Project
java -cp target/classes com.recipeplanner.SimpleSwingApp
```

### What Happens When You Run:
1. Application initializes in-memory repositories
2. Creates demo users (demo/demo123, admin/admin123)
3. Loads 5,938 Indian recipes from CSV file
4. Opens Swing GUI login window

### Demo Accounts
- **Regular User:** username: `demo` password: `demo123`
- **Admin User:** username: `admin` password: `admin123`

### Features Available:
- ✅ Login/Create Account
- ✅ Browse 5,938 recipes
- ✅ Search recipes by name/cuisine
- ✅ Sort by cooking time
- ✅ View recipe details with quantities
- ✅ Add recipes to "My Recipes"
- ✅ Multi-recipe shopping list
- ✅ Statistics dashboard

---

## Module Coverage

### Module 1: Introduction to Object-Oriented Programming
**Concepts:** Classes, Objects, Encapsulation

**Where Demonstrated:**
- **File:** `model/User.java`, `model/Ingredient.java`, `model/Recipe.java`
- **Lines:** Throughout all model classes
- **Example:**
```java
public class Ingredient {
    private int id;           // Private fields (Encapsulation)
    private String name;
    private String category;
    
    public Ingredient(String name, String category) {  // Constructor
        this.name = name;
        this.category = category;
    }
    
    public String getName() { return name; }  // Getter
    public void setName(String name) { this.name = name; }  // Setter
}
```

**Key Concepts:**
- ✅ Private fields with public getter/setter methods
- ✅ Constructors for object initialization
- ✅ Data hiding and access control

---

### Module 2: Defining Classes in Java
**Concepts:** Class design, methods, constructors, this keyword

**Where Demonstrated:**
- **Files:** All model classes, service classes, repository classes
- **Example:** `model/Recipe.java` lines 30-120
```java
public class Recipe implements Searchable {
    private int id;
    private String name;
    private String cuisine;
    private List<String> instructions;
    
    // Default constructor
    public Recipe() {
        this.instructions = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }
    
    // Parameterized constructor
    public Recipe(int id, String name, String cuisine) {
        this();  // Calling another constructor
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
    }
    
    // Method with business logic
    public void addInstruction(String instruction) {
        if (instruction != null && !instruction.trim().isEmpty()) {
            this.instructions.add(instruction.trim());
        }
    }
    
    // Method overriding equals() and hashCode()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recipe recipe = (Recipe) obj;
        return id == recipe.id;
    }
}
```

**Key Concepts:**
- ✅ Constructor overloading
- ✅ Method definitions with parameters and return types
- ✅ `this` keyword for referencing current object
- ✅ Method overriding (`equals()`, `hashCode()`, `toString()`)

---

### Module 3: Packages, Arrays, and String Handling
**Concepts:** Package organization, arrays, String manipulation

**Where Demonstrated:**

**Packages:**
- **File Structure:** All files organized in `com.recipeplanner.*` packages
```java
package com.recipeplanner.model;
package com.recipeplanner.service;
package com.recipeplanner.repository;
```

**Arrays:**
- **File:** `util/InMemoryDataSeeder.java` lines 76-84
```java
// Array demonstration
String[] spices = {"turmeric", "cumin", "coriander", "garam masala"};
String[] vegetables = {"onion", "tomato", "potato", "garlic", "ginger"};

for (String spice : spices) {  // Enhanced for loop
    Ingredient ingredient = new Ingredient(spice, "Spices");
    repository.save(ingredient);
}
```

**String Handling:**
- **File:** `service/RecipeService.java` lines 47-76
```java
public List<Recipe> searchRecipes(String searchTerm, String searchType) {
    if (searchTerm == null || searchTerm.trim().isEmpty()) {
        return repository.findAll();
    }
    
    // String manipulation: toLowerCase(), trim()
    switch (searchType.toLowerCase()) {
        case "name":
            return repository.searchByName(searchTerm.trim());
        case "cuisine":
            return repository.findByCuisine(searchTerm);
        default:
            throw new IllegalArgumentException("Invalid search type");
    }
}
```

- **File:** `util/CSVRecipeLoader.java` lines 154-175
```java
// Advanced String handling: split(), substring(), charAt()
private static String[] splitCSVLine(String line) {
    List<String> fields = new ArrayList<>();
    StringBuilder currentField = new StringBuilder();
    boolean inQuotes = false;
    
    for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);  // Character extraction
        if (c == '"') {
            inQuotes = !inQuotes;
        } else if (c == ',' && !inQuotes) {
            fields.add(currentField.toString());
            currentField = new StringBuilder();
        }
    }
    return fields.toArray(new String[0]);
}
```

**Key Concepts:**
- ✅ Package declaration and import statements
- ✅ Array declaration, initialization, and iteration
- ✅ String methods: `trim()`, `toLowerCase()`, `split()`, `substring()`, `charAt()`
- ✅ StringBuilder for efficient string manipulation

---

### Module 4: Collections and Access Control
**Concepts:** ArrayList, HashMap, LinkedHashMap, access modifiers

**Where Demonstrated:**

**ArrayList Usage:**
- **File:** `repository/UserRepository.java` lines 15-78
```java
public class UserRepository {
    private List<User> users;  // ArrayList for dynamic storage
    private int nextId = 1;
    
    public UserRepository() {
        this.users = new ArrayList<>();  // Collections initialization
    }
    
    public User save(User user) {
        if (user.getId() == 0) {
            user.setId(nextId++);
            users.add(user);  // ArrayList add operation
        } else {
            update(user);
        }
        return user;
    }
    
    public List<User> findAll() {
        return new ArrayList<>(users);  // Return defensive copy
    }
    
    public Optional<User> findByUsername(String username) {
        // Stream API with Collections
        return users.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst();
    }
}
```

**HashMap/LinkedHashMap Usage:**
- **File:** `SimpleSwingApp.java` lines 46
```java
// LinkedHashMap to maintain insertion order
private java.util.Map<String, List<String>> shoppingList = 
    new java.util.LinkedHashMap<>();

// Using Map to organize ingredients by recipe name
shoppingList.put(recipeName, ingredientsList);

// Iterating through Map entries
for (Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
    String recipeName = entry.getKey();
    List<String> ingredients = entry.getValue();
    // Process each recipe's ingredients
}
```

**Access Control Modifiers:**
```java
public class RecipeRepository {      // public: accessible everywhere
    private List<Recipe> recipes;    // private: class-level only
    protected int nextId = 1;        // protected: package + subclasses
    
    public Recipe save(Recipe recipe) {  // public method
        return validateAndSave(recipe);
    }
    
    private boolean validate(Recipe recipe) {  // private helper method
        return recipe != null && recipe.getName() != null;
    }
}
```

**Key Concepts:**
- ✅ ArrayList for dynamic lists
- ✅ HashMap/LinkedHashMap for key-value storage
- ✅ Collections Framework methods: `add()`, `remove()`, `contains()`, `stream()`
- ✅ Access modifiers: `public`, `private`, `protected`, default (package-private)

---

### Module 5: Inheritance and Polymorphism
**Concepts:** Extends, super, method overriding, runtime polymorphism

**Where Demonstrated:**

**Inheritance Hierarchy:**
- **Files:** `model/User.java`, `model/RegularUser.java`, `model/AdminUser.java`

**Base Class:**
```java
// User.java
public class User {
    protected int id;           // protected: accessible to subclasses
    protected String username;
    protected String passwordHash;
    
    public User() {}
    
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
    
    // Method to be overridden
    public String getUserType() {
        return "User";
    }
    
    // Method to be overridden
    public boolean isAdmin() {
        return false;
    }
}
```

**Subclass 1 - RegularUser:**
```java
// RegularUser.java
public class RegularUser extends User {  // Inheritance with extends
    
    public RegularUser() {
        super();  // Call parent constructor
    }
    
    public RegularUser(String username, String passwordHash) {
        super(username, passwordHash);  // Call parent parameterized constructor
    }
    
    @Override  // Method overriding annotation
    public String getUserType() {
        return "Regular User";  // Different implementation
    }
    
    @Override
    public boolean isAdmin() {
        return false;  // Regular users are not admins
    }
}
```

**Subclass 2 - AdminUser:**
```java
// AdminUser.java
public class AdminUser extends User {
    
    public AdminUser(String username, String passwordHash) {
        super(username, passwordHash);
    }
    
    @Override
    public String getUserType() {
        return "Administrator";  // Different implementation
    }
    
    @Override
    public boolean isAdmin() {
        return true;  // Admins have elevated privileges
    }
}
```

**Runtime Polymorphism in Action:**
```java
// AuthenticationService.java - lines 92-98
public User login(String username, String password) throws AuthenticationException {
    Optional<User> userOpt = userRepository.findByUsername(username);
    User user = userOpt.get();
    
    // Runtime polymorphism: getUserType() resolves at runtime
    // based on actual object type (RegularUser or AdminUser)
    System.out.println("User type: " + user.getUserType());
    System.out.println("Is admin: " + user.isAdmin());
    
    return user;  // Can return RegularUser or AdminUser (both are User)
}
```

**Key Concepts:**
- ✅ `extends` keyword for inheritance
- ✅ `super` keyword to call parent methods/constructors
- ✅ Method overriding with `@Override` annotation
- ✅ Runtime polymorphism (dynamic method dispatch)
- ✅ IS-A relationship (RegularUser IS-A User, AdminUser IS-A User)

---

### Module 6: Interfaces and Inner Classes
**Concepts:** Interface implementation, abstract methods, inner classes

**Where Demonstrated:**

**Interface Definition and Implementation:**
- **File:** `interfaces/Searchable.java`
```java
public interface Searchable {
    /**
     * Returns a formatted string for search display.
     * Must be implemented by any searchable entity.
     */
    String getSearchDisplayText();
    
    /**
     * Returns keywords for search indexing.
     */
    List<String> getSearchKeywords();
}
```

**Interface Implementation:**
- **File:** `model/Recipe.java` lines 30-50, 380-395
```java
public class Recipe implements Searchable {  // Interface implementation
    private String name;
    private String cuisine;
    private String rawIngredientsText;
    
    // Implementing interface method
    @Override
    public String getSearchDisplayText() {
        return String.format("%s (%s) - %s", 
            name, cuisine, getFormattedTime());
    }
    
    // Implementing interface method
    @Override
    public List<String> getSearchKeywords() {
        List<String> keywords = new ArrayList<>();
        keywords.add(name.toLowerCase());
        keywords.add(cuisine.toLowerCase());
        if (rawIngredientsText != null) {
            String[] ingredients = rawIngredientsText.split(",");
            for (String ing : ingredients) {
                keywords.add(ing.trim().toLowerCase());
            }
        }
        return keywords;
    }
}
```

**Inner Class (Builder Pattern):**
- **File:** `model/Recipe.java` lines 400-485
```java
public class Recipe implements Searchable {
    // ... other fields and methods ...
    
    /**
     * Inner class implementing Builder pattern.
     * Demonstrates nested class and fluent interface.
     */
    public static class RecipeBuilder {  // Static inner class
        private Recipe recipe;
        
        public RecipeBuilder() {
            this.recipe = new Recipe();
        }
        
        public RecipeBuilder withName(String name) {
            recipe.setName(name);
            return this;  // Method chaining
        }
        
        public RecipeBuilder withCuisine(String cuisine) {
            recipe.setCuisine(cuisine);
            return this;
        }
        
        public RecipeBuilder withIngredient(String ingredient) {
            recipe.addIngredient(ingredient);
            return this;
        }
        
        public Recipe build() {
            // Validation before building
            if (recipe.getName() == null || recipe.getName().trim().isEmpty()) {
                throw new IllegalStateException("Recipe name is required");
            }
            return recipe;
        }
    }
    
    // Usage of inner class
    public static RecipeBuilder builder() {
        return new RecipeBuilder();
    }
}

// Example usage:
Recipe recipe = Recipe.builder()
    .withName("Paneer Butter Masala")
    .withCuisine("North Indian")
    .withIngredient("250g paneer")
    .build();
```

**Key Concepts:**
- ✅ Interface definition with abstract methods
- ✅ `implements` keyword
- ✅ Multiple method implementation from interface
- ✅ Static inner class (RecipeBuilder)
- ✅ Fluent interface pattern with method chaining

---

### Module 7: Exception Handling
**Concepts:** try-catch, throw, throws, custom exceptions

**Where Demonstrated:**

**Custom Exception Class:**
- **File:** `exceptions/AuthenticationException.java`
```java
public class AuthenticationException extends Exception {  // Checked exception
    
    public AuthenticationException(String message) {
        super(message);  // Call parent Exception constructor
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);  // Exception chaining
    }
}
```

**Throwing Exceptions:**
- **File:** `service/AuthenticationService.java` lines 92-162
```java
public User login(String username, String password) throws AuthenticationException {
    try {
        // Input validation with exception throwing
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password cannot be empty");
        }
        
        // Business logic
        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        
        if (!userOpt.isPresent()) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        User user = userOpt.get();
        
        // Password verification
        if (!PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        return user;
        
    } catch (AuthenticationException e) {
        // Re-throw AuthenticationException as-is
        throw e;
    } catch (Exception e) {
        // Wrap other exceptions
        throw new AuthenticationException("Login failed: " + e.getMessage(), e);
    }
}
```

**Handling Exceptions:**
- **File:** `SimpleSwingApp.java` lines 170-189
```java
ActionListener loginAction = e -> {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();
    
    try {
        // Attempt login
        currentUser = authService.login(username, password);
        loginDialog.dispose();
        setVisible(true);
        loadAllRecipes();
        updateUserLabel();
        
    } catch (AuthenticationException ex) {
        // Catch specific exception
        statusLabel.setText("Login failed: " + ex.getMessage());
        ex.printStackTrace();  // Log for debugging
        
    } catch (Exception ex) {
        // Catch any other exceptions
        statusLabel.setText("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();
    }
};
```

**Multiple Exception Handling:**
```java
public void processRecipe(Recipe recipe) {
    try {
        validateRecipe(recipe);
        saveRecipe(recipe);
    } catch (IllegalArgumentException e) {
        System.err.println("Validation error: " + e.getMessage());
    } catch (NullPointerException e) {
        System.err.println("Null value encountered: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Unexpected error: " + e.getMessage());
    } finally {
        // Cleanup code runs regardless of exceptions
        System.out.println("Processing complete");
    }
}
```

**Key Concepts:**
- ✅ Custom exception classes extending Exception
- ✅ `throw` keyword to raise exceptions
- ✅ `throws` keyword in method signature
- ✅ try-catch-finally blocks
- ✅ Multiple catch blocks for different exception types
- ✅ Exception chaining and wrapping

---

## Core Concepts Demonstrated

### 1. Singleton Pattern
**File:** `repository/RepositoryManager.java`
```java
public class RepositoryManager {
    private static RepositoryManager instance;  // Static instance
    
    // Private constructor prevents instantiation
    private RepositoryManager() {
        this.userRepository = new UserRepository();
        this.recipeRepository = new RecipeRepository();
    }
    
    // Thread-safe singleton getInstance
    public static synchronized RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }
}
```

### 2. Repository Pattern
**Purpose:** Separate data access logic from business logic

**Example:** `repository/UserRepository.java`
- Provides abstraction over data storage
- Hides implementation details (ArrayList vs Database)
- Makes code testable and maintainable

### 3. Service Layer Pattern
**Purpose:** Contains business logic, coordinates between UI and data layer

**Example:** `service/AuthenticationService.java`
- Validates inputs
- Implements authentication logic
- Manages password hashing
- Throws business exceptions

### 4. MVC-like Architecture
- **Model:** `model/*` classes (data entities)
- **View:** `SimpleSwingApp.java` (UI presentation)
- **Controller:** `service/*` classes (business logic)

### 5. Defensive Copying
```java
public List<Recipe> findAll() {
    return new ArrayList<>(recipes);  // Return copy, not original
}
```

### 6. Optional Usage (Java 8+)
```java
public Optional<User> findByUsername(String username) {
    return users.stream()
        .filter(u -> u.getUsername().equalsIgnoreCase(username))
        .findFirst();  // Returns Optional<User>
}
```

### 7. Lambda Expressions and Streams
```java
// Sorting with lambda
allRecipes.sort((r1, r2) -> Integer.compare(
    r1.getTotalTimeInMins(), r2.getTotalTimeInMins()));

// Filtering with stream
List<Recipe> filtered = recipes.stream()
    .filter(r -> r.getCuisine().equals("Indian"))
    .collect(Collectors.toList());
```

---

## Class Descriptions

### Model Classes (Entities)

#### User.java
- **Purpose:** Base class for all users
- **Key Features:** Encapsulation, fields with getters/setters
- **Subclasses:** RegularUser, AdminUser

#### Recipe.java
- **Purpose:** Represents a recipe with ingredients and instructions
- **Key Features:** 
  - Implements Searchable interface
  - Contains RecipeBuilder inner class
  - ArrayList for ingredients and instructions
  - Business methods (addIngredient, addInstruction)

#### Ingredient.java
- **Purpose:** Represents a cooking ingredient
- **Key Features:** Simple encapsulation demonstration

### Repository Classes (Data Layer)

#### UserRepository.java
- **Purpose:** Manages user data in ArrayList
- **Methods:**
  - `save(User)` - Create or update
  - `findById(int)` - Retrieve by ID
  - `findByUsername(String)` - Search by username
  - `findAll()` - Get all users
  - `delete(int)` - Remove user

#### RecipeRepository.java
- **Purpose:** Manages recipe data in ArrayList
- **Methods:**
  - `save(Recipe)` - Create or update
  - `findById(int)` - Retrieve by ID
  - `searchByName(String)` - Search recipes
  - `findByCuisine(String)` - Filter by cuisine
  - `findAll()` - Get all recipes

### Service Classes (Business Logic)

#### AuthenticationService.java
- **Purpose:** Handles user authentication
- **Methods:**
  - `login(username, password)` - Authenticate user
  - `register(username, password)` - Create new account
  - `updateLastLogin(User)` - Update login timestamp
- **Exception Handling:** Throws AuthenticationException

#### RecipeService.java
- **Purpose:** Recipe business operations
- **Methods:**
  - `searchRecipes(term, type)` - Search functionality
  - `getAllRecipes()` - Retrieve all
  - `saveRecipe(Recipe)` - Persist recipe
  - `getUserRecipes(userId)` - Get user's recipes

### Utility Classes

#### PasswordHasher.java
- **Purpose:** Secure password handling
- **Methods:**
  - `hashPasswordWithNewSalt(password)` - Create hash
  - `verifyPassword(password, hash)` - Validate password
- **Security:** Uses SHA-256 with random salt

#### InMemoryDataSeeder.java
- **Purpose:** Initialize demo data
- **Demonstrates:** Arrays, ArrayList, object creation
- **Creates:** Demo users, sample ingredients

#### CSVRecipeLoader.java
- **Purpose:** Load recipes from CSV file
- **Demonstrates:** File I/O, String parsing, multi-line CSV handling
- **Loads:** 5,938 Indian recipes with ingredients and instructions

### UI Classes

#### SimpleSwingApp.java
- **Purpose:** Main Swing GUI application
- **Features:**
  - Login/Registration
  - Recipe browsing with 5,938 recipes
  - Search functionality
  - Sort by cooking time
  - Multi-recipe shopping list
  - My Recipes feature
  - User statistics
- **Demonstrates:** Event handling, layout managers, dialogs

#### SimpleConsoleApp.java
- **Purpose:** Console-based demonstration
- **Features:** Text-based menu for testing without GUI
- **Use Case:** Quick testing and demonstration

---

## Design Patterns Used

### 1. Singleton Pattern
- **Class:** `RepositoryManager`
- **Purpose:** Single instance for managing all repositories

### 2. Repository Pattern
- **Classes:** `UserRepository`, `RecipeRepository`
- **Purpose:** Abstract data access layer

### 3. Builder Pattern
- **Class:** `Recipe.RecipeBuilder`
- **Purpose:** Fluent recipe construction

### 4. Service Layer Pattern
- **Classes:** `AuthenticationService`, `RecipeService`
- **Purpose:** Centralize business logic

---

## Features Summary

### User Features
- ✅ User registration with validation
- ✅ Login authentication with password hashing
- ✅ User types: Regular User and Administrator
- ✅ Polymorphic user behavior

### Recipe Features
- ✅ Browse 5,938 Indian recipes from CSV
- ✅ Search by name, cuisine, or ingredient
- ✅ Sort recipes by cooking time
- ✅ View detailed recipe information with quantities
- ✅ Add recipes to personal collection
- ✅ View "My Recipes" separately

### Shopping List Features
- ✅ Add ingredients from multiple recipes
- ✅ Organized by recipe name
- ✅ Includes all quantities and measurements
- ✅ Clear shopping list option
- ✅ Persistent during session

### UI Features
- ✅ Clean Swing-based interface
- ✅ Login/Registration dialogs
- ✅ Recipe list with details panel
- ✅ Back button navigation
- ✅ Statistics dashboard
- ✅ Status notifications

---

## Data Storage

### In-Memory Implementation
All data is stored using Java Collections:
- **ArrayList:** For lists of users, recipes, ingredients
- **HashMap/LinkedHashMap:** For shopping list organization
- **Advantages:**
  - No external database required
  - Fast access and manipulation
  - Perfect for demonstration and learning
  - Easy to understand and maintain

### Data Initialization
- Demo users created on startup
- 5,938 recipes loaded from CSV file
- Sample ingredients seeded
- Data persists during application session

---

## Testing the Application

### Test User Login
1. Run `SimpleSwingApp`
2. Login with `demo` / `demo123` or `admin` / `admin123`
3. Verify user type is displayed correctly (polymorphism)

### Test Recipe Search
1. Use search box to find recipes
2. Try different search types
3. Verify results are filtered correctly

### Test Shopping List
1. Select multiple recipes
2. Click "Add Ingredients to List" for each
3. Click "View Shopping List"
4. Verify recipes are separated with names

### Test Create Account
1. Click "Create Account" on login
2. Enter username (min 3 chars) and password (min 6 chars)
3. Verify validation works
4. Create account and login

---

## Conclusion

This project comprehensively demonstrates all required Java OOP modules:

1. ✅ **Module 1:** OOP basics with encapsulated classes
2. ✅ **Module 2:** Class design with constructors and methods
3. ✅ **Module 3:** Packages, arrays, and String handling
4. ✅ **Module 4:** Collections (ArrayList, HashMap) and access control
5. ✅ **Module 5:** Inheritance and polymorphism with User hierarchy
6. ✅ **Module 6:** Interfaces (Searchable) and inner classes (RecipeBuilder)
7. ✅ **Module 7:** Exception handling with custom AuthenticationException

**Additional Concepts:**
- Design patterns (Singleton, Repository, Builder, Service Layer)
- File I/O and CSV parsing
- Swing GUI development
- Password security and hashing
- Lambda expressions and Streams API
- Optional usage for null safety

---

**End of Documentation**
