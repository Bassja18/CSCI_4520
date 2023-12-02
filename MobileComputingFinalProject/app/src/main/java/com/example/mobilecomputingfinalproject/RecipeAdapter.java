package com.example.mobilecomputingfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// Custom ArrayAdapter for displaying Recipe objects in a ListView
public class RecipeAdapter extends ArrayAdapter<Recipe> {

    // Context of the application
    private Context context;

    // Resource ID for the layout of each item in the ListView
    private int resource;

    // List of Recipe objects to be displayed
    private List<Recipe> recipes;

    // Constructor for the RecipeAdapter
    public RecipeAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> recipes) {
        // Call the superclass constructor (ArrayAdapter)
        super(context, resource, recipes);

        // Initialize instance variables with provided values
        this.context = context;
        this.resource = resource;
        this.recipes = recipes;
    }

    // Override the getView method to create custom views for each item in the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if a recycled view (convertView) is available, otherwise inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // Find the TextView in the inflated view by its ID
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);

        // Get the Recipe object at the current position
        Recipe recipe = recipes.get(position);

        // Set the text of the TextView to the title of the current Recipe
        textViewTitle.setText(recipe.getTitle());

        // Return the modified view to be displayed in the ListView
        return convertView;
    }
}