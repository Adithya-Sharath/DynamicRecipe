# DynamicRecipe - Recipe & Meal Planner

A comprehensive Java-based Recipe and Meal Planning application with a Swing GUI, demonstrating all core Object-Oriented Programming concepts for college curriculum.

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Swing](https://img.shields.io/badge/GUI-Swing-blue.svg)
![Storage](https://img.shields.io/badge/Storage-In--Memory-green.svg)
![Recipes](https://img.shields.io/badge/Recipes-5938-brightgreen.svg)

## 🎯 Project Overview

This application is built to demonstrate all 7 modules of Java Object-Oriented Programming:
1. **OOP Basics** - Classes, Objects, Encapsulation
2. **Class Design** - Constructors, Methods, Overloading
3. **Packages & Strings** - Package organization, String handling, Arrays
4. **Collections** - ArrayList, HashMap, LinkedHashMap
5. **Inheritance & Polymorphism** - User hierarchy with method overriding
6. **Interfaces & Inner Classes** - Searchable interface, RecipeBuilder inner class
7. **Exception Handling** - Custom AuthenticationException

## ✨ Features

- 🔐 **User Authentication** - Login/Registration with password hashing
- 📚 **5,938 Indian Recipes** - Loaded from CSV dataset
- 🔍 **Smart Search** - Search by recipe name or cuisine
- ⏱️ **Sort by Time** - Find quick recipes
- ❤️ **My Recipes** - Personal recipe collection
- 🛒 **Shopping List** - Multi-recipe ingredient aggregation
- 📊 **Statistics** - User activity dashboard
- 👥 **User Types** - Regular users and Administrators (polymorphism)

## 🚀 Quick Start

### Prerequisites
- Java JDK 11 or higher
- No external dependencies required!

### Run the Application

```bash
cd DynamicRecipe
java -cp target/classes com.recipeplanner.SimpleSwingApp
```

### Demo Accounts
- **Regular User:** `demo` / `demo123`
- **Admin User:** `admin` / `admin123`

## 📁 Project Structure

```
DynamicRecipe/
├── src/main/java/com/recipeplanner/
│   ├── SimpleSwingApp.java           # Main Swing GUI application
│   ├── SimpleConsoleApp.java         # Console demo application
│   │
│   ├── model/                        # Entity classes
│   │   ├── User.java                 # Base class for polymorphism
│   │   ├── RegularUser.java          # Inherits from User
│   │   ├── AdminUser.java            # Inherits from User
│   │   ├── Recipe.java               # Implements Searchable
│   │   ├── Ingredient.java
│   │   ├── MealType.java             # Enum
│   │   └── Measurement.java
│   │
│   ├── repository/                   # Data access layer
│   │   ├── UserRepository.java
│   │   ├── RecipeRepository.java
│   │   ├── IngredientRepository.java
│   │   └── RepositoryManager.java    # Singleton pattern
│   │
│   ├── service/                      # Business logic
│   │   ├── AuthenticationService.java
│   │   └── RecipeService.java
│   │
│   ├── util/                         # Utilities
│   │   ├── PasswordHasher.java
│   │   ├── InMemoryDataSeeder.java
│   │   └── CSVRecipeLoader.java
│   │
│   ├── interfaces/
│   │   └── Searchable.java           # Interface definition
│   │
│   └── exceptions/
│       └── AuthenticationException.java
│
├── Cleaned_Indian_Food_Dataset.csv   # 5,938 recipes
├── PROJECT_DOCUMENTATION.md          # Detailed module coverage
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## 🎓 Educational Value

This project demonstrates:

### Module 1: OOP Basics
- Encapsulation with private fields and public methods
- Object creation and initialization

### Module 2: Class Design
- Multiple constructors
- Method overloading
- `this` keyword usage
- `equals()`, `hashCode()`, `toString()` overriding

### Module 3: Packages, Arrays & Strings
- Package organization (`com.recipeplanner.*`)
- Array declaration and iteration
- String methods: `trim()`, `split()`, `toLowerCase()`, etc.
- StringBuilder for efficient concatenation

### Module 4: Collections & Access Control
- `ArrayList` for dynamic storage
- `HashMap`/`LinkedHashMap` for key-value pairs
- Access modifiers: `public`, `private`, `protected`
- Stream API operations

### Module 5: Inheritance & Polymorphism
- User → RegularUser, AdminUser hierarchy
- Method overriding with `@Override`
- Runtime polymorphism (dynamic method dispatch)
- `super` keyword

### Module 6: Interfaces & Inner Classes
- `Searchable` interface implementation
- `RecipeBuilder` static inner class
- Fluent interface pattern

### Module 7: Exception Handling
- Custom `AuthenticationException`
- try-catch-finally blocks
- Exception chaining
- Input validation with exceptions

## 🎨 Design Patterns

- **Singleton** - RepositoryManager ensures single instance
- **Repository** - Abstracts data access from business logic
- **Builder** - Recipe.RecipeBuilder for fluent object creation
- **Service Layer** - Separates business logic from presentation

## 📸 Screenshots

### Login Screen
Clean and simple authentication interface with demo account hints.

### Recipe Browser
Browse through 5,938 Indian recipes with search and sort functionality.

### Shopping List
Add ingredients from multiple recipes, organized by recipe name.

## 🛠️ Development

### Compile from Source

```bash
# Compile all files
javac -d target/classes -sourcepath src/main/java src/main/java/com/recipeplanner/SimpleSwingApp.java

# Run application
java -cp target/classes com.recipeplanner.SimpleSwingApp
```

### Using Maven

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.recipeplanner.SimpleSwingApp"
```

## 📊 Data Storage

- **Storage Type:** In-Memory (No database required)
- **Collections:** ArrayList, HashMap, LinkedHashMap
- **Data Persistence:** Session-based (resets on restart)
- **Recipe Source:** CSV file with 5,938 Indian recipes

## 🤝 Contributing

This is an educational project for college curriculum. Contributions for educational improvements are welcome!

## 📝 License

This project is created for educational purposes as part of college coursework.

## 👨‍💻 Author

**Adithya Sharath**
- GitHub: [@Adithya-Sharath](https://github.com/Adithya-Sharath)

## 🙏 Acknowledgments

- Indian Food Dataset: Cleaned and curated recipe collection
- Java Swing: GUI framework
- College OOP Curriculum: Project requirements and module specifications

---

## 📚 Documentation

For detailed module coverage, code examples, and concept explanations, see [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)

---

**⭐ If this helps you with your OOP learning, please star the repository!**
