package com.example.mobilecomputingfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientAdder extends AppCompatActivity {

    private EditText editIngredientEditText, editIngredientMeasurmentEditText;
    private Button addAnotherBtn, doneAddingBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_adder);

        editIngredientEditText = findViewById(R.id.editIngredientText);
        editIngredientMeasurmentEditText = findViewById(R.id.editIngredientMeasurmentText);
        addAnotherBtn = findViewById(R.id.addAnotherBtn);
        doneAddingBtn = findViewById(R.id.doneAddingBtn);

        doneAddingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainView();
            }
        });
    }

    private void openMainView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
