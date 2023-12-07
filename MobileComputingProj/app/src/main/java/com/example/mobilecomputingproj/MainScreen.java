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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    private List<Recipe> recipeList = new ArrayList<>();

    private ListView recipeListView;

    private Button addRecipeBtn;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_view);

        addRecipeBtn = findViewById(R.id.addRecipeBtn);
        recipeListView = findViewById(R.id.recipeListView);

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
                        String recipeTitle = data.getStringExtra("recipeTitle");
                        String recipeDescription = data.getStringExtra("recipeDescription");
                        String ingredientName = data.getStringExtra("ingredientName");
                        String meaSpinInStr = data.getStringExtra("mea");
                        String amouSpinInStr = data.getStringExtra("amo");
                        String ingList = data.getStringExtra("ingredientList");

                        Recipe newRecipe = new Recipe(recipeTitle, recipeDescription, ingredientName,meaSpinInStr, amouSpinInStr, ingList);
                        recipeList.add(newRecipe);

                        Collections.sort(recipeList, new Comparator<Recipe>() {
                            @Override
                            public int compare(Recipe recipe1, Recipe recipe2) {
                                return recipe1.getTitle().compareToIgnoreCase(recipe2.getTitle());
                            }
                        });


                        RecipeAdapter adapter = new RecipeAdapter(MainScreen.this, R.layout.recipe_item, recipeList);
                        recipeListView.setAdapter(adapter);

                        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Recipe selectedRecipe = recipeList.get(i);
                                showRecipeDetails(selectedRecipe);
                            }
                        });
                    }
                }
            }
    );

    private void showRecipeDetails(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipe.getTitle());

        String messageText = recipe.getDescription();
        messageText += recipe.getIngList();

        SpannableString spannableString = new SpannableString(messageText);

        builder.setMessage(spannableString);

        builder.setPositiveButton("OK", null);
        builder.show();
    }
}


