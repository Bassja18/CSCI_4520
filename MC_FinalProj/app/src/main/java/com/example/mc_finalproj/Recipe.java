package com.example.mc_finalproj;

public class Recipe {

    //Initialize Variables
    //Title of the recipe
    private String title;

    //Description of the recipe
    private String description;

    //Ingredients in the recipe
    private String ingredient;

    //Ingredients of the recipe
    //Might need a list here

    // Default constructor required for converting Firebase DataSnapshot to Recipe object
    public Recipe() {
        // Default constructor is necessary for DataSnapshot.getValue(Recipe.class)
    }

    public Recipe(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Recipe(String title, String description, String ingredient) {
        this.title = title;
        this.description = description;
        this.ingredient = ingredient;
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