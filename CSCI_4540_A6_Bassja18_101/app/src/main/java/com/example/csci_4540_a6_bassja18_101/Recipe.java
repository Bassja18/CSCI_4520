package com.example.csci_4540_a6_bassja18_101;

// Model class representing a Recipe
public class Recipe {

    // Unique identifier for the recipe
    private String id;

    // Title of the recipe
    private String title;

    // Description of the recipe
    private String description;

    // Default constructor required for converting Firebase DataSnapshot to Recipe object
    public Recipe() {
        // Default constructor is necessary for DataSnapshot.getValue(Recipe.class)
    }

    // Parameterized constructor to initialize a Recipe with provided values
    public Recipe(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getter method for retrieving the ID of the recipe
    public String getId() {
        return id;
    }

    // Getter method for retrieving the title of the recipe
    public String getTitle() {
        return title;
    }

    // Getter method for retrieving the description of the recipe
    public String getDescription() {
        return description;
    }
}
