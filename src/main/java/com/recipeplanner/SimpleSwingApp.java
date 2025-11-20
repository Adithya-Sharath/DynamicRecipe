package com.recipeplanner;

import com.recipeplanner.model.*;
import com.recipeplanner.service.AuthenticationService;
import com.recipeplanner.service.RecipeService;
import com.recipeplanner.util.InMemoryDataSeeder;
import com.recipeplanner.util.CSVRecipeLoader;
import com.recipeplanner.exceptions.AuthenticationException;
import com.recipeplanner.repository.RepositoryManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.Window;

/**
 * Simple Swing-based GUI for Recipe & Meal Planner.
 * Demonstrates simple UI design with all menu-driven functions.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Swing UI)
 */
public class SimpleSwingApp extends JFrame {
    
    // Services
    private AuthenticationService authService = new AuthenticationService();
    private RecipeService recipeService = new RecipeService();
    
    // Current user
    private User currentUser;
    
    // UI Components
    private JList<String> recipeList;
    private DefaultListModel<String> recipeListModel;
    private JTextArea recipeDetailsArea;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JButton backButton;
    
    // Store all recipes for reference
    private List<Recipe> allRecipes;
    
    // Shopping list to accumulate ingredients from multiple recipes
    private java.util.Map<String, List<String>> shoppingList = new java.util.LinkedHashMap<>();
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            SimpleSwingApp app = new SimpleSwingApp();
            app.showLoginDialog();
        });
    }
    
    /**
     * Constructor - initializes the main window.
     */
    public SimpleSwingApp() {
        setTitle("Recipe & Meal Planner");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize data
        initializeData();
        
        // Create UI components
        createUI();
    }
    
    /**
     * Initializes in-memory data storage and loads all recipes from CSV.
     */
    private void initializeData() {
        System.out.println("Initializing data...");
        
        try {
            // Seed basic data (users, sample ingredients)
            InMemoryDataSeeder seeder = new InMemoryDataSeeder();
            if (!seeder.isDataSeeded()) {
                seeder.createDemoUsers();
                seeder.createSampleIngredients();
                System.out.println("Demo users created");
            }
            
            // Load ALL recipes from CSV
            System.out.println("Loading recipes from CSV...");
            int recipeCount = CSVRecipeLoader.loadRecipesFromCSV(
                RepositoryManager.getInstance().getRecipeRepository()
            );
            System.out.println("Loaded " + recipeCount + " recipes from CSV");
            
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows login dialog.
     */
    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Exit application if login dialog is closed
        loginDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Recipe & Meal Planner");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Demo accounts info
        JLabel infoLabel = new JLabel("<html><i>Demo: demo/demo123 or admin/admin123</i></html>");
        gbc.gridy = 1;
        panel.add(infoLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton loginButton = new JButton("Login");
        buttonsPanel.add(loginButton);
        
        JButton createAccountButton = new JButton("Create Account");
        buttonsPanel.add(createAccountButton);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonsPanel, gbc);
        
        // Status label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        panel.add(statusLabel, gbc);
        
        // Login action
        ActionListener loginAction = e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter username and password");
                return;
            }
            
            try {
                currentUser = authService.login(username, password);
                loginDialog.dispose();
                setVisible(true);
                loadAllRecipes();
                updateUserLabel();
            } catch (AuthenticationException ex) {
                statusLabel.setText("Login failed: " + ex.getMessage());
            }
        };
        
        // Create account action
        createAccountButton.addActionListener(e -> {
            loginDialog.setVisible(false);
            showCreateAccountDialog();
            loginDialog.dispose();
        });
        
        loginButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        
        loginDialog.add(panel);
        loginDialog.setVisible(true);
    }
    
    /**
     * Shows create account dialog.
     * Demonstrates user registration and exception handling.
     */
    private void showCreateAccountDialog() {
        JDialog createDialog = new JDialog(this, "Create New Account", true);
        createDialog.setSize(450, 350);
        createDialog.setLocationRelativeTo(this);
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Instructions
        JLabel instructLabel = new JLabel("<html><i>Username: min 3 characters<br>Password: min 6 characters</i></html>");
        gbc.gridy = 1;
        panel.add(instructLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Confirm Password:"), gbc);
        
        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton createButton = new JButton("Create Account");
        buttonsPanel.add(createButton);
        
        JButton cancelButton = new JButton("Cancel");
        buttonsPanel.add(cancelButton);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonsPanel, gbc);
        
        // Status label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 6;
        panel.add(statusLabel, gbc);
        
        // Create account action
        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Username and password cannot be empty");
                return;
            }
            
            if (username.length() < 3) {
                statusLabel.setText("Username must be at least 3 characters");
                return;
            }
            
            if (password.length() < 6) {
                statusLabel.setText("Password must be at least 6 characters");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                statusLabel.setText("Passwords do not match");
                return;
            }
            
            // Try to register
            try {
                User newUser = authService.register(username, password);
                
                JOptionPane.showMessageDialog(createDialog,
                    "Account created successfully!\nYou can now login with username: " + newUser.getUsername(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                createDialog.dispose();
                showLoginDialog();
                
            } catch (AuthenticationException ex) {
                statusLabel.setText(ex.getMessage());
            }
        });
        
        // Cancel action
        cancelButton.addActionListener(e -> {
            createDialog.dispose();
            showLoginDialog();
        });
        
        createDialog.add(panel);
        createDialog.setVisible(true);
    }
    
    /**
     * Creates the main UI.
     */
    private void createUI() {
        setLayout(new BorderLayout(5, 5));
        
        // Top panel - toolbar
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - split pane with list and details
        JSplitPane splitPane = createCenterPanel();
        add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel - status bar
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates top toolbar panel.
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // User info
        userLabel = new JLabel();
        panel.add(userLabel, BorderLayout.WEST);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRecipes());
        searchPanel.add(searchButton);
        
        JButton clearButton = new JButton("Show All");
        clearButton.addActionListener(e -> loadAllRecipes());
        searchPanel.add(clearButton);
        
        // Sort by time button
        JButton sortByTimeButton = new JButton("Sort by Time");
        sortByTimeButton.addActionListener(e -> sortRecipesByTime());
        searchPanel.add(sortByTimeButton);
        
        panel.add(searchPanel, BorderLayout.CENTER);
        
        // Menu buttons panel
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Back button (initially hidden, shown when viewing My Recipes)
        backButton = new JButton("← Back to All Recipes");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(e -> {
            loadAllRecipes();
            backButton.setVisible(false);
        });
        backButton.setVisible(false);
        menuPanel.add(backButton);
        
        JButton myRecipesBtn = new JButton("My Recipes");
        myRecipesBtn.addActionListener(e -> showMyRecipes());
        menuPanel.add(myRecipesBtn);
        
        JButton shoppingListBtn = new JButton("View Shopping List");
        shoppingListBtn.addActionListener(e -> viewShoppingList());
        menuPanel.add(shoppingListBtn);
        
        JButton statsBtn = new JButton("Statistics");
        statsBtn.addActionListener(e -> showStatistics());
        menuPanel.add(statsBtn);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        menuPanel.add(logoutBtn);
        
        panel.add(menuPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates center panel with recipe list and details.
     */
    private JSplitPane createCenterPanel() {
        // Left panel - recipe list
        recipeListModel = new DefaultListModel<>();
        recipeList = new JList<>(recipeListModel);
        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedRecipe();
            }
        });
        
        JScrollPane listScroll = new JScrollPane(recipeList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Recipes"));
        
        // Right panel - recipe details with action buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        recipeDetailsArea = new JTextArea();
        recipeDetailsArea.setEditable(false);
        recipeDetailsArea.setLineWrap(true);
        recipeDetailsArea.setWrapStyleWord(true);
        recipeDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane detailsScroll = new JScrollPane(recipeDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Recipe Details"));
        rightPanel.add(detailsScroll, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton addToMyRecipesBtn = new JButton("Add to My Recipes");
        addToMyRecipesBtn.addActionListener(e -> addToMyRecipes());
        actionPanel.add(addToMyRecipesBtn);
        
        JButton addIngredientsBtn = new JButton("Add Ingredients to List");
        addIngredientsBtn.addActionListener(e -> addIngredientsToList());
        actionPanel.add(addIngredientsBtn);
        
        rightPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, rightPanel);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        return splitPane;
    }
    
    /**
     * Creates bottom status panel.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        statusLabel = new JLabel("Ready");
        panel.add(statusLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Updates user label with current user info.
     */
    private void updateUserLabel() {
        if (currentUser != null) {
            userLabel.setText("Logged in as: " + currentUser.getUsername() + 
                            " (" + currentUser.getUserType() + ")");
        }
    }
    
    /**
     * Loads all recipes into the list.
     */
    private void loadAllRecipes() {
        allRecipes = recipeService.getAllRecipes();
        updateRecipeList(allRecipes);
        statusLabel.setText("Loaded " + allRecipes.size() + " recipes");
    }
    
    /**
     * Updates the recipe list display.
     * Shows only recipe names for cleaner UI.
     */
    private void updateRecipeList(List<Recipe> recipes) {
        recipeListModel.clear();
        for (Recipe recipe : recipes) {
            // Show only recipe name
            recipeListModel.addElement(recipe.getName());
        }
        
        if (!recipes.isEmpty()) {
            recipeList.setSelectedIndex(0);
        }
    }
    
    /**
     * Shows details of the selected recipe.
     */
    private void showSelectedRecipe() {
        int index = recipeList.getSelectedIndex();
        if (index >= 0 && index < allRecipes.size()) {
            Recipe recipe = allRecipes.get(index);
            displayRecipeDetails(recipe);
        }
    }
    
    /**
     * Displays recipe details in the text area with proper formatting.
     */
    private void displayRecipeDetails(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        
        // Recipe Title - handle long names with wrapping
        sb.append("╔═══════════════════════════════════════════════════╗\n");
        
        String recipeName = recipe.getName();
        if (recipeName.length() <= 48) {
            // Short name - center it
            sb.append("║ ").append(centerText(recipeName, 50)).append(" ║\n");
        } else {
            // Long name - wrap to multiple lines and left-align for consistency
            String[] wrappedLines = wrapRecipeTitle(recipeName, 48);
            for (String line : wrappedLines) {
                sb.append("║ ").append(padRight(line, 50)).append(" ║\n");
            }
        }
        
        sb.append("╚═══════════════════════════════════════════════════╝\n\n");
        
        // Basic Information
        sb.append("┌─ BASIC INFORMATION ─────────────────────────────┐\n");
        sb.append("│ Cuisine:       ").append(recipe.getCuisine()).append("\n");
        sb.append("│ Cooking Time:  ").append(recipe.getFormattedTime()).append("\n");
        sb.append("└─────────────────────────────────────────────────┘\n\n");
        
        // Ingredients Section - with quantities
        if (recipe.getRawIngredientsText() != null && !recipe.getRawIngredientsText().isEmpty()) {
            sb.append("┌─ INGREDIENTS (with quantities) ─────────────────┐\n");
            
            // Split ingredients by comma - preserve quantities
            String[] ingredients = recipe.getRawIngredientsText().split(",");
            for (int i = 0; i < ingredients.length; i++) {
                String ingredient = ingredients[i].trim();
                if (!ingredient.isEmpty()) {
                    // Display with quantity included
                    sb.append("│ • ").append(ingredient).append("\n");
                }
            }
            sb.append("└─────────────────────────────────────────────────┘\n\n");
        }
        
        // Instructions Section
        if (!recipe.getInstructions().isEmpty()) {
            sb.append("┌─ COOKING INSTRUCTIONS ──────────────────────────┐\n");
            List<String> instructions = recipe.getInstructions();
            for (int i = 0; i < instructions.size(); i++) {
                String instruction = instructions.get(i).trim();
                if (!instruction.isEmpty()) {
                    sb.append("│\n");
                    sb.append("│ Step ").append(i + 1).append(":\n");
                    sb.append("│ ").append(wrapText(instruction, 50)).append("\n");
                }
            }
            sb.append("└─────────────────────────────────────────────────┘\n");
        }
        
        recipeDetailsArea.setText(sb.toString());
        recipeDetailsArea.setCaretPosition(0);
    }
    
    /**
     * Wraps recipe title to multiple lines.
     */
    private String[] wrapRecipeTitle(String title, int maxWidth) {
        if (title.length() <= maxWidth) {
            return new String[] { title };
        }
        
        java.util.List<String> lines = new java.util.ArrayList<>();
        String[] words = title.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Word itself is too long, truncate it
                    lines.add(word.substring(0, maxWidth));
                    currentLine = new StringBuilder();
                }
            }
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines.toArray(new String[0]);
    }
    
    /**
     * Pads text on the right to specified width (left-aligned).
     */
    private String padRight(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    /**
     * Centers text for display.
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - leftPadding;
        
        StringBuilder sb = new StringBuilder();
        
        // Add left padding
        for (int i = 0; i < leftPadding; i++) {
            sb.append(" ");
        }
        
        // Add text
        sb.append(text);
        
        // Add right padding
        for (int i = 0; i < rightPadding; i++) {
            sb.append(" ");
        }
        
        return sb.toString();
    }
    
    /**
     * Wraps text to specified width.
     */
    private String wrapText(String text, int width) {
        if (text.length() <= width) {
            return text;
        }
        
        StringBuilder sb = new StringBuilder();
        String[] words = text.split(" ");
        int lineLength = 0;
        
        for (String word : words) {
            if (lineLength + word.length() + 1 > width) {
                sb.append("\n│ ");
                lineLength = 0;
            }
            if (lineLength > 0) {
                sb.append(" ");
                lineLength++;
            }
            sb.append(word);
            lineLength += word.length();
        }
        
        return sb.toString();
    }
    
    /**
     * Searches for recipes.
     */
    private void searchRecipes() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadAllRecipes();
            return;
        }
        
        List<Recipe> results = recipeService.searchRecipes(searchTerm, "name");
        allRecipes = results;
        updateRecipeList(results);
        statusLabel.setText("Found " + results.size() + " recipe(s) matching '" + searchTerm + "'");
    }
    
    /**
     * Shows only user's recipes.
     */
    private void showMyRecipes() {
        List<Recipe> myRecipes = recipeService.getUserRecipes(currentUser.getId());
        allRecipes = myRecipes;
        updateRecipeList(myRecipes);
        statusLabel.setText("Your recipes: " + myRecipes.size());
        
        // Show back button and refresh the panel
        if (backButton != null) {
            backButton.setVisible(true);
            backButton.getParent().revalidate();
            backButton.getParent().repaint();
        }
    }
    
    /**
     * Shows statistics dialog.
     */
    private void showStatistics() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        
        // Count by cuisine
        int northIndian = 0;
        int southIndian = 0;
        int other = 0;
        
        for (Recipe recipe : recipes) {
            String cuisine = recipe.getCuisine();
            if (cuisine != null) {
                if (cuisine.toLowerCase().contains("north")) {
                    northIndian++;
                } else if (cuisine.toLowerCase().contains("south")) {
                    southIndian++;
                } else {
                    other++;
                }
            }
        }
        
        String message = String.format(
            "Total Recipes: %d\n\n" +
            "By Cuisine:\n" +
            "  North Indian: %d\n" +
            "  South Indian: %d\n" +
            "  Other: %d\n\n" +
            "User: %s (%s)\n" +
            "Admin Privileges: %s",
            recipes.size(),
            northIndian,
            southIndian,
            other,
            currentUser.getUsername(),
            currentUser.getUserType(),
            currentUser.isAdmin() ? "Yes" : "No"
        );
        
        JOptionPane.showMessageDialog(this, message, "Statistics", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Logs out and shows login dialog.
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            setVisible(false);
            currentUser = null;
            recipeListModel.clear();
            recipeDetailsArea.setText("");
            showLoginDialog();
        }
    }
    
    /**
     * Sorts recipes by cooking time (shortest to longest).
     * Demonstrates Collections sorting and Comparator usage.
     */
    private void sortRecipesByTime() {
        if (allRecipes == null || allRecipes.isEmpty()) {
            statusLabel.setText("No recipes to sort");
            return;
        }
        
        // Sort by cooking time using Java 8 lambda
        allRecipes.sort((r1, r2) -> Integer.compare(r1.getTotalTimeInMins(), r2.getTotalTimeInMins()));
        
        updateRecipeList(allRecipes);
        statusLabel.setText("Recipes sorted by cooking time (shortest first)");
    }
    
    /**
     * Adds the currently selected recipe to user's recipe collection.
     * Demonstrates copying recipes and user-specific data.
     */
    private void addToMyRecipes() {
        int index = recipeList.getSelectedIndex();
        if (index < 0 || index >= allRecipes.size()) {
            JOptionPane.showMessageDialog(this,
                "Please select a recipe first",
                "No Recipe Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Recipe selectedRecipe = allRecipes.get(index);
        
        // Create a copy of the recipe for the user
        Recipe userRecipe = new Recipe();
        userRecipe.setName(selectedRecipe.getName() + " (My Copy)");
        userRecipe.setCuisine(selectedRecipe.getCuisine());
        userRecipe.setTotalTimeInMins(selectedRecipe.getTotalTimeInMins());
        userRecipe.setRawIngredientsText(selectedRecipe.getRawIngredientsText());
        userRecipe.setCreatedBy(currentUser.getId());
        
        // Copy instructions
        for (String instruction : selectedRecipe.getInstructions()) {
            userRecipe.addInstruction(instruction);
        }
        
        // Save to repository
        recipeService.saveRecipe(userRecipe);
        
        JOptionPane.showMessageDialog(this,
            "Recipe added to your collection!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText("Added '" + selectedRecipe.getName() + "' to My Recipes");
    }
    
    /**
     * Adds ingredients from the selected recipe to the persistent shopping list.
     * Multiple recipes can be added and ingredients are organized by recipe name.
     */
    private void addIngredientsToList() {
        int index = recipeList.getSelectedIndex();
        if (index < 0 || index >= allRecipes.size()) {
            JOptionPane.showMessageDialog(this,
                "Please select a recipe first",
                "No Recipe Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Recipe selectedRecipe = allRecipes.get(index);
        String ingredientsText = selectedRecipe.getRawIngredientsText();
        
        if (ingredientsText == null || ingredientsText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "This recipe has no ingredients listed",
                "No Ingredients",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Parse ingredients and add to shopping list
        String[] ingredients = ingredientsText.split(",");
        List<String> ingredientsList = new java.util.ArrayList<>();
        
        for (String ingredient : ingredients) {
            String trimmed = ingredient.trim();
            if (!trimmed.isEmpty()) {
                ingredientsList.add(trimmed);
            }
        }
        
        // Add to shopping list map (organized by recipe name)
        shoppingList.put(selectedRecipe.getName(), ingredientsList);
        
        // Show confirmation
        JOptionPane.showMessageDialog(this,
            "Added " + ingredientsList.size() + " ingredients from:\n\"" + 
            selectedRecipe.getName() + "\"\n\nClick 'View Shopping List' to see all ingredients.",
            "Added to Shopping List",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText("Added " + ingredientsList.size() + " ingredients from '" + 
                          selectedRecipe.getName() + "' to shopping list");
    }
    
    /**
     * Displays the accumulated shopping list with ingredients separated by recipe.
     * Demonstrates Map usage and organized data display.
     */
    private void viewShoppingList() {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Shopping list is empty.\n\nSelect a recipe and click 'Add Ingredients to List' to add items.",
                "Empty Shopping List",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Build shopping list display
        StringBuilder listDisplay = new StringBuilder();
        listDisplay.append("╔════════════════════════════════════════════════╗\n");
        listDisplay.append("║      SHOPPING LIST - MULTIPLE RECIPES          ║\n");
        listDisplay.append("╚════════════════════════════════════════════════╝\n\n");
        
        int totalItems = 0;
        int recipeCount = 0;
        
        // Display ingredients organized by recipe
        for (java.util.Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            recipeCount++;
            String recipeName = entry.getKey();
            List<String> ingredients = entry.getValue();
            
            listDisplay.append("┌─ RECIPE ").append(recipeCount).append(": ");
            listDisplay.append(recipeName).append("\n");
            listDisplay.append("└─────────────────────────────────────────────────\n");
            
            for (String ingredient : ingredients) {
                listDisplay.append("  ☐ ").append(ingredient).append("\n");
                totalItems++;
            }
            
            listDisplay.append("\n");
        }
        
        listDisplay.append("═══════════════════════════════════════════════\n");
        listDisplay.append("Total Recipes: ").append(recipeCount).append("\n");
        listDisplay.append("Total Ingredients: ").append(totalItems).append("\n");
        listDisplay.append("═══════════════════════════════════════════════");
        
        // Create dialog with shopping list
        JTextArea textArea = new JTextArea(listDisplay.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        // Create panel with buttons
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton clearButton = new JButton("Clear Shopping List");
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Clear all items from shopping list?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shoppingList.clear();
                JOptionPane.showMessageDialog(this,
                    "Shopping list cleared!",
                    "Cleared",
                    JOptionPane.INFORMATION_MESSAGE);
                Window window = SwingUtilities.getWindowAncestor(clearButton);
                if (window != null) {
                    window.dispose();
                }
            }
        });
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this,
            panel,
            "Shopping List - " + recipeCount + " Recipe(s)",
            JOptionPane.PLAIN_MESSAGE);
    }
}
