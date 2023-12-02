package com.example.mc_finalproj;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initialize Variables
    private EditText editTextTitle, editTextDescription;
    private Button btnAddRecipe, addIngBtn;
    private ListView listViewRecipes;

    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference recipeRef;

    private String title, description, tempRecipeString, ingredient;

    private Recipe recipe;

    private byte[] data;

    private UploadTask uploadTask;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize UI elements to be used
        //Edit Texts
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);

        //Buttons
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        addIngBtn = findViewById(R.id.addIngredientBtn);

        //List View
        listViewRecipes = findViewById(R.id.listViewRecipes);

        //Get reference to the "Recipes" node in the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

        //Get FirebaseStorage instance
        storage = FirebaseStorage.getInstance();

        //Get reference to the root of your Firebase Storage
        recipeRef = storage.getReference();


        //Button Functionality
        //Set a click listener for the "Add Recipe" button
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to add a new recipe
                addRecipe();
            }
        });

        //Set a click listener for the "Add Ingredients" button
        addIngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, IngredientsActivity.class);
                startActivity(intent);
            }
        });

    }


    private void addRecipe() {

        // Get user input for title and description
        title = editTextTitle.getText().toString().trim();
        description = editTextDescription.getText().toString().trim();

        ingredient = getIntent().getStringExtra("name");

        if (ingredient == null || ingredient == "")
        {
            ingredient = "";
        }



        if (title.isEmpty() || description.isEmpty()) {
            // Display a message if title or description is empty
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
        } else {
            // Create a Recipe object
            recipe = new Recipe(title, description, ingredient);

            //Create a temp String recipe to use with Byte. Add format for database.
            tempRecipeString = "Recipe: " + title + "\nDescription: " + description;

            //Convert recipe to bytes to push to database.
            data = tempRecipeString.getBytes(StandardCharsets.UTF_8);

            //Storage Reference to upload
            if (ingredient == "") {
                recipeRef = storage.getReference().child("Recipes").child(title).child(description);
            }
            else
            {
                recipeRef = storage.getReference().child("Recipes").child(title).child(description).child(ingredient);
            }

            //Upload data to firebase storage
            uploadTask = recipeRef.putBytes(data);

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
        }

    }
}