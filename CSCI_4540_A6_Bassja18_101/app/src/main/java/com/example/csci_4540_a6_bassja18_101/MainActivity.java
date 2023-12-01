package com.example.csci_4540_a6_bassja18_101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button btnAddRecipe;
    private ListView listViewRecipes;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        listViewRecipes = findViewById(R.id.listViewRecipes);

        // Get reference to the "recipes" node in the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");

        // Set a click listener for the "Add Recipe" button
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to add a new recipe
                addRecipe();

                // Pause for 1 second to ensure data consistency (not recommended in practice)
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Load and display all recipes after adding a new one
                loadRecipes();
            }
        });

        // Load and display all recipes when the activity is created
        loadRecipes();
    }

    // Method to add a new recipe to Firebase Realtime Database and Firebase Storage
    private void addRecipe() {
        // Get user input for title and description
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Check if both title and description are not empty
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
            // Generate a unique key for the new recipe
            String recipeId = databaseReference.push().getKey();

            // Initialize Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Create a Recipe object
            Recipe recipe = new Recipe(recipeId, title, description);

            // Prepare data for storage
            String tempTitle = recipe.getTitle();
            String tempDesc = recipe.getDescription();
            String tempRecipe = "Recipe: " + tempTitle + "\nDescription: " + tempDesc;
            byte[] data = tempRecipe.getBytes(StandardCharsets.UTF_8);

            // Define references for title and description in Firebase Storage
            StorageReference titleAndDescriptionRef = storage.getReference().child(tempTitle);
            StorageReference titleRef = titleAndDescriptionRef.child(recipe.getTitle() + ".txt");
            StorageReference descriptionRef = titleAndDescriptionRef.child(recipe.getDescription() + "_description.txt");

            // Upload data to Firebase Storage
            UploadTask uploadTask = titleAndDescriptionRef.putBytes(data);

            // Clear input fields
            editTextTitle.setText("");
            editTextDescription.setText("");

            // Set listeners for upload success and failure
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Recipe added successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error adding recipe", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Display a message if title or description is empty
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load recipes from Firebase Storage and display them in a ListView
    private void loadRecipes() {
        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // List all items in the root of Firebase Storage
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final List<Recipe> recipes = new ArrayList<>();

                // Iterate through each item in the storage
                for (StorageReference item : listResult.getItems()) {
                    // Extract title from the item name
                    String title = item.getName().replace(".txt", "");

                    // Retrieve content of the recipe from Firebase Storage
                    item.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Convert bytes to String (recipe description)
                            String description = new String(bytes);
                            Recipe recipe = new Recipe(null, title, description);
                            recipes.add(recipe);

                            // Sort the recipes alphabetically
                            Collections.sort(recipes, new Comparator<Recipe>() {
                                @Override
                                public int compare(Recipe recipe1, Recipe recipe2) {
                                    return recipe1.getTitle().compareToIgnoreCase(recipe2.getTitle());
                                }
                            });

                            // Create an adapter for the ListView and set it
                            RecipeAdapter adapter = new RecipeAdapter(MainActivity.this, R.layout.recipe_item, recipes);
                            listViewRecipes.setAdapter(adapter);

                            // Set item click listener to show details in an AlertDialog
                            listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Recipe selectedRecipe = recipes.get(i);
                                    showRecipeDetails(selectedRecipe);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to load recipe content", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to display details of a recipe in an AlertDialog
    private void showRecipeDetails(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipe.getTitle());
        builder.setMessage(recipe.getDescription());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
