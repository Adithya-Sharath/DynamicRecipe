package com.recipeplanner;

import com.recipeplanner.model.*;
import com.recipeplanner.service.AuthenticationService;
import com.recipeplanner.service.RecipeService;
import com.recipeplanner.util.InMemoryDataSeeder;
import com.recipeplanner.exceptions.AuthenticationException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.InputStream;

/**
 * Simple Swing-based GUI for Recipe & Meal Planner.
 * Demonstrates simple UI design with all menu-driven functions.
 * 
 * @author Recipe Planner Team
 * @version 2.0 (Swing UI)
 */
public class SimpleSwingApp extends JFrame {
    
    // Custom Lexend font
    private static Font LEXEND_FONT;
    
    // Services
    private AuthenticationService authService = new AuthenticationService();
    private RecipeService recipeService = new RecipeService();
    
    // Current user
    private User currentUser;
    
    // UI Components
    private JPanel recipeCardsPanel;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JButton backButton;
    private JButton removeRecipeBtn;
    
    // Store all recipes for reference
    private List<Recipe> allRecipes;
    
    // Shopping list to accumulate ingredients from multiple recipes
    private java.util.Map<String, List<String>> shoppingList = new java.util.LinkedHashMap<>();
    
    public static void main(String[] args) {
        // Load custom Lexend font
        loadCustomFont();
        
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
     * Loads the custom Lexend font from resources.
     */
    private static void loadCustomFont() {
        try {
            InputStream fontStream = SimpleSwingApp.class.getResourceAsStream("/fonts/Lexend-Bold.ttf");
            if (fontStream != null) {
                LEXEND_FONT = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(LEXEND_FONT);
                fontStream.close();
                System.out.println("Lexend font loaded successfully");
            } else {
                System.out.println("Font file not found, using fallback font");
                LEXEND_FONT = new Font("SansSerif", Font.PLAIN, 12);
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
            LEXEND_FONT = new Font("SansSerif", Font.PLAIN, 12);
        }
    }
    
    /**
     * Gets the Lexend font with specified style and size.
     */
    private static Font getLexendFont(int style, float size) {
        if (LEXEND_FONT != null) {
            return LEXEND_FONT.deriveFont(style, size);
        }
        return new Font("SansSerif", style, (int) size);
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
     * Initializes data storage with MySQL and loads recipes from database.
     */
    private void initializeData() {
        try {
            // Initialize all data (users, ingredients, and MySQL recipes)
            InMemoryDataSeeder seeder = new InMemoryDataSeeder();
            seeder.seedAllData();
            
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
        setLayout(new BorderLayout());
        
        // Top panel - toolbar (simplified, shown only in My Recipes view)
        JPanel topPanel = createTopPanel();
        topPanel.setVisible(false); // Hide by default, show only for My Recipes
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - modern card grid
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
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
    
    // Mint green theme colors
    private static final Color MINT_BG = new Color(232, 245, 233);
    private static final Color DARK_GREEN = new Color(46, 125, 50);
    
    /**
     * Creates center panel with modern recipe cards grid.
     */
    private JPanel createCenterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MINT_BG);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MINT_BG);
        headerPanel.setBorder(new EmptyBorder(30, 40, 20, 40));
        
        // Left side - Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(MINT_BG);
        
        JLabel titleLabel = new JLabel("Recipe Book");
        titleLabel.setFont(getLexendFont(Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Discover authentic Indian flavors");
        subtitleLabel.setFont(getLexendFont(Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_GREEN);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Right side - Action buttons
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionButtonsPanel.setBackground(MINT_BG);
        
        // Back button (hidden by default, shown when viewing My Recipes)
        backButton = createOutlineButton("<< All Recipes");
        backButton.setVisible(false);
        backButton.addActionListener(e -> loadAllRecipes());
        actionButtonsPanel.add(backButton);
        
        JButton myRecipesBtn = createOutlineButton("View my recipes");
        myRecipesBtn.addActionListener(e -> showMyRecipes());
        actionButtonsPanel.add(myRecipesBtn);
        
        JButton ingredientListBtn = createOutlineButton("View my ingredient list");
        ingredientListBtn.addActionListener(e -> viewShoppingList());
        actionButtonsPanel.add(ingredientListBtn);
        
        headerPanel.add(actionButtonsPanel, BorderLayout.EAST);
        
        // Search and Sort row
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(MINT_BG);
        
        // Search bar
        searchField = new JTextField(25);
        searchField.setText("Search recipes...");
        searchField.setFont(getLexendFont(Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setPreferredSize(new Dimension(350, 40));
        searchField.setBorder(createRoundedBorder(new Color(200, 200, 200), 15, 10));
        searchField.setBackground(Color.WHITE);
        
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search recipes...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(33, 33, 33));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search recipes...");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });
        searchField.addActionListener(e -> searchRecipes());
        searchPanel.add(searchField);
        
        // Sort button
        JButton sortBtn = createOutlineButton("Sort by Time");
        sortBtn.addActionListener(e -> sortRecipesByTime());
        searchPanel.add(sortBtn);
        
        // Combine header
        JPanel fullHeaderPanel = new JPanel(new BorderLayout());
        fullHeaderPanel.setBackground(MINT_BG);
        fullHeaderPanel.add(headerPanel, BorderLayout.NORTH);
        fullHeaderPanel.add(searchPanel, BorderLayout.SOUTH);
        
        mainPanel.add(fullHeaderPanel, BorderLayout.NORTH);
        
        // Recipe cards panel - single column layout
        recipeCardsPanel = new JPanel();
        recipeCardsPanel.setLayout(new BoxLayout(recipeCardsPanel, BoxLayout.Y_AXIS));
        recipeCardsPanel.setBackground(MINT_BG);
        recipeCardsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JScrollPane scrollPane = new JScrollPane(recipeCardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(MINT_BG);
        scrollPane.setBackground(MINT_BG);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Creates an outlined button with consistent style.
     */
    private JButton createOutlineButton(String text) {
        JButton button = new JButton(text);
        button.setFont(getLexendFont(Font.PLAIN, 13));
        button.setForeground(new Color(60, 60, 60));
        button.setBackground(Color.WHITE);
        button.setBorder(createRoundedBorder(new Color(180, 180, 180), 12, 8));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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
        
        // Hide back button when viewing all recipes
        if (backButton != null) {
            backButton.setVisible(false);
        }
        if (removeRecipeBtn != null) {
            removeRecipeBtn.setVisible(false);
        }
    }
    
    /**
     * Updates the recipe cards display - single column with spacing.
     */
    private void updateRecipeList(List<Recipe> recipes) {
        recipeCardsPanel.removeAll();
        
        for (Recipe recipe : recipes) {
            JPanel card = createRecipeCard(recipe);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height + 50));
            recipeCardsPanel.add(card);
            recipeCardsPanel.add(Box.createVerticalStrut(15)); // Spacing between cards
        }
        
        recipeCardsPanel.revalidate();
        recipeCardsPanel.repaint();
    }
    
    /**
     * Creates a modern recipe card matching the design.
     */
    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(createRoundedBorder(new Color(220, 220, 220), 15, 18));
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Recipe name
        JLabel nameLabel = new JLabel(recipe.getName());
        nameLabel.setFont(getLexendFont(Font.BOLD, 18));
        nameLabel.setForeground(new Color(33, 33, 33));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(nameLabel);
        
        contentPanel.add(Box.createVerticalStrut(12));
        
        // Meta info panel (time and servings)
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setBackground(Color.WHITE);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Time label
        JLabel timeLabel = new JLabel(recipe.getFormattedTime());
        timeLabel.setFont(getLexendFont(Font.PLAIN, 13));
        timeLabel.setForeground(new Color(100, 100, 100));
        metaPanel.add(timeLabel);
        
        contentPanel.add(metaPanel);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Ingredients count
        int ingredientCount = recipe.getRawIngredientsText() != null ? 
            recipe.getRawIngredientsText().split(",").length : 0;
        JLabel ingredientsLabel = new JLabel(ingredientCount + " ingredients");
        ingredientsLabel.setFont(getLexendFont(Font.PLAIN, 13));
        ingredientsLabel.setForeground(DARK_GREEN);
        ingredientsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(ingredientsLabel);
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add to Grocery button
        JButton groceryButton = createOutlineButton("Add to Grocery");
        groceryButton.setFont(getLexendFont(Font.PLAIN, 11));
        groceryButton.addActionListener(e -> addRecipeToGroceryList(recipe));
        buttonsPanel.add(groceryButton);
        
        // View Recipe button
        JButton viewButton = createOutlineButton("View Recipe");
        viewButton.setFont(getLexendFont(Font.PLAIN, 11));
        viewButton.addActionListener(e -> showModernRecipeCard(recipe));
        buttonsPanel.add(viewButton);
        
        // Add to My Recipes button
        JButton addToMyBtn = createOutlineButton("+ My Recipes");
        addToMyBtn.setFont(getLexendFont(Font.PLAIN, 11));
        addToMyBtn.addActionListener(e -> addToMyRecipesAction(recipe));
        buttonsPanel.add(addToMyBtn);
        
        // Delete button (red X)
        JButton deleteButton = new JButton("X");
        deleteButton.setFont(getLexendFont(Font.BOLD, 12));
        deleteButton.setForeground(new Color(211, 47, 47));
        deleteButton.setBackground(new Color(255, 235, 238));
        deleteButton.setBorder(createRoundedBorder(new Color(211, 47, 47), 10, 6));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove '" + recipe.getName() + "' from your recipes?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                recipeService.deleteRecipe(recipe.getId());
                loadAllRecipes();
            }
        });
        buttonsPanel.add(deleteButton);
        
        contentPanel.add(buttonsPanel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Adds recipe ingredients to grocery/shopping list.
     */
    private void addRecipeToGroceryList(Recipe recipe) {
        String ingredientsText = recipe.getRawIngredientsText();
        
        if (ingredientsText == null || ingredientsText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "This recipe has no ingredients listed",
                "No Ingredients",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] ingredients = ingredientsText.split(",");
        List<String> ingredientsList = new java.util.ArrayList<>();
        
        for (String ingredient : ingredients) {
            String trimmed = ingredient.trim();
            if (!trimmed.isEmpty()) {
                ingredientsList.add(trimmed);
            }
        }
        
        shoppingList.put(recipe.getName(), ingredientsList);
        
        JOptionPane.showMessageDialog(this,
            "Added " + ingredientsList.size() + " ingredients from:\n\"" + 
            recipe.getName() + "\"\n\nClick 'View my ingredient list' to see all.",
            "Added to Grocery List",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText("Added " + ingredientsList.size() + " ingredients to grocery list");
    }
    
    /**
     * Adds a recipe to user's personal collection.
     */
    private void addToMyRecipesAction(Recipe recipe) {
        // Create a copy of the recipe for the user
        Recipe userRecipe = new Recipe.RecipeBuilder(recipe.getName())
            .withCuisine(recipe.getCuisine())
            .withCookingTime(recipe.getTotalTimeInMins())
            .withDescription(recipe.getDescription())
            .createdBy(currentUser.getId())
            .build();
        
        userRecipe.setRawIngredientsText(recipe.getRawIngredientsText());
        for (String instruction : recipe.getInstructions()) {
            userRecipe.addInstruction(instruction);
        }
        
        recipeService.saveRecipe(userRecipe);
        
        JOptionPane.showMessageDialog(this,
            "'" + recipe.getName() + "' has been added to your recipes!\n\nClick 'View my recipes' to see your collection.",
            "Added to My Recipes",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText("Added '" + recipe.getName() + "' to your recipes");
    }
    
    /**
     * Shows recipe in a modern card design dialog with green theme.
     */
    private void showModernRecipeCard(Recipe recipe) {
        JDialog dialog = new JDialog(this, "Recipe Details", true);
        dialog.setUndecorated(true); // Remove title bar (no duplicate close button)
        dialog.setSize(700, 750);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Main panel with white background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        
        // Close button panel
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closePanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("X");
        closeButton.setFont(getLexendFont(Font.BOLD, 14));
        closeButton.setForeground(new Color(100, 100, 100));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(createRoundedBorder(new Color(180, 180, 180), 10, 5));
        closeButton.setPreferredSize(new Dimension(35, 35));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        closePanel.add(closeButton);
        
        mainPanel.add(closePanel, BorderLayout.NORTH);
        
        // Content panel with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Recipe Title
        JLabel titleLabel = new JLabel(recipe.getName());
        titleLabel.setFont(getLexendFont(Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Time
        JLabel timeLabel = new JLabel(recipe.getFormattedTime());
        timeLabel.setFont(getLexendFont(Font.PLAIN, 14));
        timeLabel.setForeground(new Color(100, 100, 100));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(timeLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        
        // Ingredients Section
        JLabel ingredientsTitle = new JLabel("Ingredients");
        ingredientsTitle.setFont(getLexendFont(Font.BOLD, 18));
        ingredientsTitle.setForeground(new Color(33, 33, 33));
        ingredientsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(ingredientsTitle);
        contentPanel.add(Box.createVerticalStrut(12));
        
        // Ingredients list with green bullets
        if (recipe.getRawIngredientsText() != null && !recipe.getRawIngredientsText().isEmpty()) {
            String[] ingredients = recipe.getRawIngredientsText().split(",");
            for (String ingredient : ingredients) {
                if (!ingredient.trim().isEmpty()) {
                    JPanel ingredientPanel = createGreenIngredientItem(ingredient.trim());
                    ingredientPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentPanel.add(ingredientPanel);
                    contentPanel.add(Box.createVerticalStrut(10));
                }
            }
        }
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(220, 220, 220));
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Instructions Section
        JLabel instructionsTitle = new JLabel("Instructions");
        instructionsTitle.setFont(getLexendFont(Font.BOLD, 18));
        instructionsTitle.setForeground(new Color(33, 33, 33));
        instructionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(instructionsTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Instructions with numbered circles
        List<String> instructions = recipe.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            JPanel instructionPanel = createInstructionItem(i + 1, instructions.get(i));
            instructionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(instructionPanel);
            contentPanel.add(Box.createVerticalStrut(12));
        }
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Creates an ingredient item with green bullet point.
     */
    private JPanel createGreenIngredientItem(String ingredient) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        
        // Green bullet
        JLabel bullet = new JLabel("●");
        bullet.setFont(new Font("Arial", Font.PLAIN, 10));
        bullet.setForeground(DARK_GREEN);
        panel.add(bullet);
        
        panel.add(Box.createHorizontalStrut(12));
        
        // Ingredient text
        JLabel text = new JLabel(ingredient);
        text.setFont(getLexendFont(Font.PLAIN, 14));
        text.setForeground(new Color(80, 80, 80));
        panel.add(text);
        
        return panel;
    }
    
    /**
     * Creates an instruction item with green numbered circle.
     */
    private JPanel createInstructionItem(int number, String instruction) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        
        // Green numbered circle
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK_GREEN);
                g2.fillOval(0, 0, 28, 28);
                g2.setColor(Color.WHITE);
                g2.setFont(getLexendFont(Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String num = String.valueOf(number);
                int x = (28 - fm.stringWidth(num)) / 2;
                int y = ((28 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(num, x, y);
            }
        };
        circlePanel.setPreferredSize(new Dimension(28, 28));
        circlePanel.setMinimumSize(new Dimension(28, 28));
        circlePanel.setMaximumSize(new Dimension(28, 28));
        circlePanel.setBackground(Color.WHITE);
        
        panel.add(circlePanel, BorderLayout.WEST);
        
        // Instruction text
        JLabel textLabel = new JLabel(instruction);
        textLabel.setFont(getLexendFont(Font.PLAIN, 14));
        textLabel.setForeground(new Color(80, 80, 80));
        panel.add(textLabel, BorderLayout.CENTER);
        
        return panel;
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
        
        // Show remove button in My Recipes view
        if (removeRecipeBtn != null) {
            removeRecipeBtn.setVisible(true);
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
            recipeCardsPanel.removeAll();
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
     * Displays the shopping list in a modern mint-themed dialog.
     */
    private void viewShoppingList() {
        if (shoppingList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Shopping list is empty.\n\nClick 'Add to Grocery' on any recipe to add ingredients.",
                "Empty Shopping List",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create modern dialog
        JDialog dialog = new JDialog(this, "My Ingredient List", true);
        dialog.setUndecorated(true); // Remove title bar (no duplicate close button)
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Main panel with mint background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MINT_BG);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MINT_BG);
        
        JLabel titleLabel = new JLabel("Ingredient List");
        titleLabel.setFont(getLexendFont(Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Close button
        JButton closeBtn = new JButton("X");
        closeBtn.setFont(getLexendFont(Font.BOLD, 14));
        closeBtn.setForeground(new Color(100, 100, 100));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorder(createRoundedBorder(new Color(180, 180, 180), 10, 5));
        closeBtn.setPreferredSize(new Dimension(35, 35));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dialog.dispose());
        headerPanel.add(closeBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(MINT_BG);
        contentPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        int totalItems = 0;
        
        // Display ingredients organized by recipe in cards
        for (java.util.Map.Entry<String, List<String>> entry : shoppingList.entrySet()) {
            String recipeName = entry.getKey();
            List<String> ingredients = entry.getValue();
            
            // Recipe card
            JPanel recipeCard = new JPanel();
            recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.Y_AXIS));
            recipeCard.setBackground(Color.WHITE);
            recipeCard.setBorder(createRoundedBorder(new Color(220, 220, 220), 15, 15));
            recipeCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Recipe name
            JLabel recipeLabel = new JLabel(recipeName);
            recipeLabel.setFont(getLexendFont(Font.BOLD, 16));
            recipeLabel.setForeground(DARK_GREEN);
            recipeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            recipeCard.add(recipeLabel);
            recipeCard.add(Box.createVerticalStrut(10));
            
            // Ingredients
            for (String ingredient : ingredients) {
                JPanel ingredientRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
                ingredientRow.setBackground(Color.WHITE);
                ingredientRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel bullet = new JLabel("*");
                bullet.setFont(getLexendFont(Font.BOLD, 12));
                bullet.setForeground(DARK_GREEN);
                ingredientRow.add(bullet);
                ingredientRow.add(Box.createHorizontalStrut(10));
                
                JLabel ingredientLabel = new JLabel(ingredient);
                ingredientLabel.setFont(getLexendFont(Font.PLAIN, 13));
                ingredientLabel.setForeground(new Color(80, 80, 80));
                ingredientRow.add(ingredientLabel);
                
                recipeCard.add(ingredientRow);
                totalItems++;
            }
            
            contentPanel.add(recipeCard);
            contentPanel.add(Box.createVerticalStrut(15));
        }
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(MINT_BG);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer with summary and clear button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(MINT_BG);
        footerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JLabel summaryLabel = new JLabel("Total: " + totalItems + " ingredients from " + shoppingList.size() + " recipe(s)");
        summaryLabel.setFont(getLexendFont(Font.PLAIN, 13));
        summaryLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(summaryLabel, BorderLayout.WEST);
        
        JButton clearButton = createOutlineButton("Clear All");
        clearButton.setForeground(new Color(211, 47, 47));
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Clear all items from shopping list?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shoppingList.clear();
                dialog.dispose();
                statusLabel.setText("Shopping list cleared");
            }
        });
        footerPanel.add(clearButton, BorderLayout.EAST);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Creates a rounded border with specified color and radius.
     */
    private static javax.swing.border.Border createRoundedBorder(Color color, int radius, int padding) {
        return BorderFactory.createCompoundBorder(
            new RoundedBorder(color, radius),
            new EmptyBorder(padding, padding, padding, padding)
        );
    }
    
    /**
     * Custom rounded border implementation.
     */
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int radius;
        
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
