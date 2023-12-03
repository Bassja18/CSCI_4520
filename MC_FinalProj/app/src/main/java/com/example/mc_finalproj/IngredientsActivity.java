package com.example.mc_finalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {

    private EditText editTextIngredientName;
    private Spinner spinnerMeasurement;
    private Spinner spinnerUnitSize;
    private Button buttonAddAnother;
    private Button buttonAdd;

    private String name, measurement, size;

    private Intent intent;

    private List<String> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_layout);

        // Initialize views by finding them using their IDs
        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        spinnerMeasurement = findViewById(R.id.spinnerMeasurement);
        spinnerUnitSize = findViewById(R.id.spinnerUnitSize);
        buttonAddAnother = findViewById(R.id.buttonAddAnother);
        buttonAdd = findViewById(R.id.buttonAdd);

        ingredientList = new ArrayList<String>();




        buttonAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = editTextIngredientName.getText().toString().trim();
                // Get selected values from spinners
                measurement = spinnerMeasurement.getSelectedItem().toString();
                size = spinnerUnitSize.getSelectedItem().toString();

                ingredientList.add(name);
                ingredientList.add(measurement);
                ingredientList.add(size);

                editTextIngredientName.setText("");

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextIngredientName.getText().toString().trim();
                // Get selected values from spinners
                measurement = spinnerMeasurement.getSelectedItem().toString();
                size = spinnerUnitSize.getSelectedItem().toString();

                ingredientList.add(name);
                ingredientList.add(measurement);
                ingredientList.add(size);

                // Create an intent and pass data
                intent = new Intent(IngredientsActivity.this, MainActivity.class);
                intent.putStringArrayListExtra("name", (ArrayList<String>) ingredientList);

                // Start the MainActivity
                startActivity(intent);
            }
        });



    }
}