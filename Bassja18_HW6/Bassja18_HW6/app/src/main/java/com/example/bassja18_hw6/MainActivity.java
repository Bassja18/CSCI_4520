package com.example.bassja18_hw6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore fire_storage;
    private Button add_recipeBtn;
    private String recipeTitle = "";
    private String recipeDescription = "";
    private EditText recipeTitleTextArea, recipeDescriptionTextArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_recipeBtn = findViewById(R.id.addRecipeBtn);
        recipeTitleTextArea = findViewById(R.id.recipeTitleTextArea);
        recipeDescriptionTextArea = findViewById(R.id.recipeDescriptionTextArea);

        add_recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recipeTitle = recipeTitleTextArea.getText().toString();
                recipeDescription = recipeDescriptionTextArea.getText().toString();

                fire_storage = FirebaseFirestore.getInstance();


                Map<String, String> recipes = new HashMap<>();
                recipes.put(recipeTitle, recipeDescription);

                fire_storage.collection("Recipes").add(recipes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Success",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}