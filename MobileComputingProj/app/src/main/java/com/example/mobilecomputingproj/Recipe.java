package com.example.mobilecomputingproj;

import android.widget.Spinner;

public class Recipe {
    private String title;
    private String description;
    private String ingredientName;

    private String ingList;

    private String measSpin, amouSpin;

    public Recipe(String title, String description, String ingredientName, String measSpin, String amouSpin, String ingList) {
        this.title = title;
        this.description = description;
        this.ingredientName = ingredientName;
        this.measSpin = measSpin;
        this.amouSpin = amouSpin;
        this.ingList = ingList;
    }

    public Recipe(String ingredientName, String measSpin, String amouSpin) {
        this.ingredientName = ingredientName;
        this.measSpin = measSpin;
        this.amouSpin = amouSpin;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getMeasSpin() {
        return measSpin;
    }

    public String getAmouSpin() {
        return amouSpin;
    }

    public String getIngList() {
        return ingList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("").append(title != null ? title : "").append("\n");
        stringBuilder.append("").append(description != null ? description : "").append("\n\n");
        stringBuilder.append("Ingredient Name: ").append(ingredientName != null ? ingredientName : "").append("\n");
        stringBuilder.append("Measurement: ").append(measSpin != null ? measSpin : "").append("\n");
        stringBuilder.append("Amount: ").append(amouSpin != null ? amouSpin : "").append("\n");
        stringBuilder.append("").append(ingList != null ? ingList : "").append("\n");

        return stringBuilder.toString();
    }

}
