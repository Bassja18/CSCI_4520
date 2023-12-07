package com.example.mobilecomputingproj;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    private List<Recipe> recipeList = new ArrayList<>();

    private ListView recipeListView;

    private Button addRecipeBtn;

    private Intent intent;

    private String recipeTitle, recipeDescription, ingredientName, meaSpinInStr, amouSpinInStr, ingList, messageText, recipeId, tempRecipe;

    private DatabaseReference databaseReference;

    private FirebaseStorage storage;

    private StorageReference storageRef, titleRef, descRef, ingListRef, ingRef, meaSpinRef, amuSpinRef;

    private Recipe newRecipe, selectedRecipe;

    private RecipeAdapter adapter;

    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_view);

        addRecipeBtn = findViewById(R.id.addRecipeBtn);
        recipeListView = findViewById(R.id.recipeListView);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainScreen.this, AddRecipeCard.class);
                addRecipeLauncher.launch(intent);
            }
        });
    }

    private final ActivityResultLauncher<Intent> addRecipeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        recipeTitle = data.getStringExtra("recipeTitle");
                        recipeDescription = data.getStringExtra("recipeDescription");
                        ingredientName = data.getStringExtra("ingredientName");
                        meaSpinInStr = data.getStringExtra("mea");
                        amouSpinInStr = data.getStringExtra("amo");
                        ingList = data.getStringExtra("ingredientList");

                        newRecipe = new Recipe(recipeTitle, recipeDescription, ingredientName, meaSpinInStr, amouSpinInStr, ingList);
                        recipeList.add(newRecipe);

                        Collections.sort(recipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe recipe1, Recipe recipe2) {
                                return recipe1.getTitle().compareToIgnoreCase(recipe2.getTitle());
                            }
                        });

                        adapter = new RecipeAdapter(MainScreen.this, R.layout.recipe_item, recipeList);
                        recipeListView.setAdapter(adapter);

                        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedRecipe = recipeList.get(i);
                                showRecipeDetails(selectedRecipe);
                            }
                        });

                        addRecipes();
                    }
                }
            }
    );

    private void showRecipeDetails(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipe.getTitle());

        messageText = recipe.getDescription();
        messageText += recipe.getIngList();

        SpannableString spannableString = new SpannableString(messageText);

        builder.setMessage(spannableString);

        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void addRecipes() {
        if (!recipeTitle.isEmpty() && !recipeDescription.isEmpty()) {

            recipeId = databaseReference.push().getKey();

            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            newRecipe = new Recipe(recipeTitle, recipeDescription, ingredientName, meaSpinInStr, amouSpinInStr, ingList);

            tempRecipe = "Recipe: " + recipeTitle + "\nDescription: " + recipeDescription + "\nIngredient: " + ingredientName + "\nMeasurement: " + meaSpinInStr + "\nAmount: " + amouSpinInStr + "\nIngredient List: " + ingList;

            byte[] data = tempRecipe.getBytes(StandardCharsets.UTF_8);

            titleRef = storage.getReference().child(recipeTitle);
            descRef = titleRef.child(recipeDescription);
            ingRef = descRef.child(ingredientName);
            meaSpinRef = ingRef.child(meaSpinInStr);
            amuSpinRef = meaSpinRef.child(amouSpinInStr);
            ingListRef = amuSpinRef.child(ingList);


            uploadTask = ingListRef.putBytes(data);

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
}


