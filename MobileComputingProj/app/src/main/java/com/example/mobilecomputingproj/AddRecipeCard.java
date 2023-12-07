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

    private String name, description, ingredientName, meaSpinStr, amoSpinStr, ingrNameTemp;

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

                name = editRecipeTitle.getText().toString().trim();
                description = editTextDescription.getText().toString().trim();
                ingredientName = editTextIngredientName.getText().toString().trim();
                meaSpinStr = spinnerMeasurement.getSelectedItem().toString();
                amoSpinStr = spinnerUnitSize.getSelectedItem().toString();

                intent.putExtra("recipeTitle", name);
                intent.putExtra("recipeDescription", description);

                if (!ingredientArray.isEmpty())
                {
                    if (ingredientName.isEmpty())
                    {
                        intent.putExtra("ingredientName", ingrNameTemp);
                        intent.putExtra("mea", meaSpinStr);
                        intent.putExtra("amo", amoSpinStr);
                        String ingredientArrayStr = convertIngredientArrayToString(ingredientArray);
                        ingredientArrayStr = ingredientArrayStr.replace("\n", "");
                        intent.putExtra("ingredientList", ingredientArrayStr);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else
                    {
                        intent.putExtra("ingredientName", ingredientName);
                        intent.putExtra("mea", meaSpinStr);
                        intent.putExtra("amo", amoSpinStr);
                        String ingredientArrayStr = convertIngredientArrayToString(ingredientArray);
                        ingredientArrayStr = ingredientArrayStr.replace("\n", "");
                        intent.putExtra("ingredientList", ingredientArrayStr);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                else
                {
                    intent.putExtra("ingredientName", ingredientName);
                    intent.putExtra("mea", meaSpinStr);
                    intent.putExtra("amo", amoSpinStr);
                    String ingredientArrayStr = ingredientName + " " + meaSpinStr + " " + amoSpinStr;
                    intent.putExtra("ingredientList", ingredientArrayStr);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
                ingredientName = editTextIngredientName.getText().toString().trim();
                meaSpinStr = spinnerMeasurement.getSelectedItem().toString();
                amoSpinStr = spinnerUnitSize.getSelectedItem().toString();

                Recipe tempRecipe = new Recipe(ingredientName, meaSpinStr, amoSpinStr);

                ingredientArray.add(tempRecipe);

                ingrNameTemp = ingredientName;

                editTextIngredientName.setText("");
            }
        });
    }
}