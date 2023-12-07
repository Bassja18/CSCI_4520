package com.example.mc_finalproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView card1, card2, card3, card4;
    public TextView textCard1, textCard2, textCard3, textCard4;
    //Initialize Variables
    private EditText editTextTitle, editTextDescription;
    private Button btnAddRecipe, addIngBtn;
    //private ListView listViewRecipes;

    private DatabaseReference databaseReference;
    private StorageReference recipeRef;
    private FirebaseStorage storage;

    private String title, description, tempRecipeString, ingredient, ingredientName, ingredientMeasurement, ingredientSize;

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

        // Cards
        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);
        card3 = (CardView) findViewById(R.id.c3);
        card4 = (CardView) findViewById(R.id.c4);
        textCard1 = (TextView) findViewById(R.id.c1text);
        textCard2 = (TextView) findViewById(R.id.c2text);
        textCard3 = (TextView) findViewById(R.id.c3text);
        textCard4 = (TextView) findViewById(R.id.c4text);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);

        //Buttons
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        addIngBtn = findViewById(R.id.addIngredientBtn);

        //List View
        //listViewRecipes = findViewById(R.id.listViewRecipes);

        //Get reference to the "Recipes" node in the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

        //Get FirebaseStorage instance
        storage = FirebaseStorage.getInstance();

        //Get reference to the root of your Firebase Storage
        recipeRef = storage.getReference();

        //recipeRef = FirebaseStorage.getInstance().getReference();


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
                intent = new Intent(MainActivity.this, AddRecipe.class);
                intent.putExtra("title", editTextTitle.getText().toString().trim());
                intent.putExtra("description", editTextDescription.getText().toString().trim());
                startActivity(intent);
            }
        });
        // Used to display the card after entering ingredients in the "Ingredients Activity"
        if (getIntent().getBooleanExtra("showCard", false)) {
            String name = getIntent().getStringExtra("title");
            // Show a new card only if the previous one is visible
            // Couldnt get this to work so we should probably trash it and just have the one that will
            // update when user inputs
            if (card1.getVisibility() != View.VISIBLE) {
                textCard1.setText(name);
                card1.setVisibility(View.VISIBLE);
            } else if (card2.getVisibility() != View.VISIBLE) {
                textCard2.setText(name);
                card2.setVisibility(View.VISIBLE);
            } else if (card3.getVisibility() != View.VISIBLE) {
                textCard3.setText(name);
                card3.setVisibility(View.VISIBLE);
            } else if (card4.getVisibility() != View.VISIBLE) {
                textCard4.setText(name);
                card4.setVisibility(View.VISIBLE);
            }
        }
    }


    private void addRecipe() {
        // Get user input for title and description
        title = editTextTitle.getText().toString().trim();
        description = editTextDescription.getText().toString().trim();

        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("name");

        //Check if ingredients is null before using it
        if (ingredients != null) {
            ingredient = TextUtils.join(", ", ingredients);
        } else {
            ingredient = "";
        }

        //I placed these two lines in the if statement to keep the app from crashing if you leave
        // the title and descriptions empty then click on the add recipe button
        //ingredient = TextUtils.join(", ", ingredients);
        //ingredient = formatString(ingredient);

        //Don't remember if these were commented out or not so check behind me plz
        //ingredientName = getIntent().getStringExtra("name");
        //ingredientMeasurement = getIntent().getStringExtra("measurement");
        //ingredientSize = getIntent().getStringExtra("size");

        //ingredient = ingredientName + " " + ingredientMeasurement + " " + ingredientSize;

        //Think we can get rid of this since I included the if else statement 146-149
        if (ingredient == null || ingredient.isEmpty())
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

    private static String formatString(String input) {
        String[] numbers = input.split(",\\s*");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numbers.length; i++) {
            result.append(numbers[i]);
            if ((i + 1) % 3 == 0 && i < numbers.length - 1) {
                result.append(", ");
            } else if (i < numbers.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    @Override
    public void onClick(View view) {
        Intent i;
        if (view.getId() == R.id.c1) {
            i = new Intent(this, RecipeCard.class);
            startActivity(i);
        }
        if (view.getId() == R.id.c2) {
            i = new Intent(this, RecipeCard.class);
            startActivity(i);
        }
        if (view.getId() == R.id.c3) {
            i = new Intent(this, RecipeCard.class);
            startActivity(i);
        }
        if (view.getId() == R.id.c4) {
            i = new Intent(this, RecipeCard.class);
            startActivity(i);
        }
    }
}