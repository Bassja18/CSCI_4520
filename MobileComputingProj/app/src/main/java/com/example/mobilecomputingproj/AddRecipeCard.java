package com.example.mobilecomputingproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeCard extends AppCompatActivity {

    private EditText editRecipeTitle, editTextDescription, editTextIngredientName;

    private Spinner spinnerMeasurement, spinnerUnitSize;

    private Button recipeCompleteBtn, addAnotherIngredientBtn;

    private ArrayList<Recipe> ingredientArray;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_card_view);


        editRecipeTitle = findViewById(R.id.editRecipeTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextIngredientName = findViewById(R.id.editTextIngredientName);

        spinnerMeasurement = findViewById(R.id.spinnerMeasurement);
        spinnerUnitSize = findViewById(R.id.spinnerUnitSize);

        recipeCompleteBtn = findViewById(R.id.recipeCompleteBtn);
        addAnotherIngredientBtn = findViewById(R.id.addAnotherIngredientBtn);

        ingredientArray = new ArrayList<>();

        recipeCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddRecipeCard.this, MainScreen.class);

                String name = editRecipeTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String ingredientName = editTextIngredientName.getText().toString().trim();
                String meaSpinStr = spinnerMeasurement.getSelectedItem().toString();
                String amoSpinStr = spinnerUnitSize.getSelectedItem().toString();

                intent.putExtra("recipeTitle", name);
                intent.putExtra("recipeDescription", description);

                if (!ingredientArray.isEmpty())
                {
                    String ingredientArrayStr = convertIngredientArrayToString(ingredientArray);
                    intent.putExtra("ingredientList", ingredientArrayStr);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    intent.putExtra("ingredientName", ingredientName);
                    intent.putExtra("mea", meaSpinStr);
                    intent.putExtra("amo", amoSpinStr);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                //Todo: Might have to clear recycler view on this click if it keeps old recipe ingredients.

                //Todo: Send recipe to database
            }

            private String convertIngredientArrayToString(ArrayList<Recipe> ingredientArray) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Recipe recipe : ingredientArray) {
                    if (recipe != null) {
                        String recipeString = recipe.toString();
                        if (recipeString != null && !recipeString.isEmpty()) {
                            stringBuilder.append(recipeString).append("\n");
                        }
                    }
                }
                return stringBuilder.toString();
            }

        });

        addAnotherIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: Populate recycler view with ingredient on click. Possibly refresh recycler after button click if its not auto populated.
                String ingredientName = editTextIngredientName.getText().toString().trim();
                String meaSpinStr = spinnerMeasurement.getSelectedItem().toString();
                String amoSpinStr = spinnerUnitSize.getSelectedItem().toString();

                Recipe tempRecipe = new Recipe(ingredientName, meaSpinStr, amoSpinStr);

                ingredientArray.add(tempRecipe);

                editTextIngredientName.setText("");
            }
        });
    }
}