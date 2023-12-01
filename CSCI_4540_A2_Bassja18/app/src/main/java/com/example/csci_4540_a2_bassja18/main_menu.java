package com.example.csci_4540_a2_bassja18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main_menu extends AppCompatActivity {

    private Button question_1_button, question_2_button, question_3_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_view);

        question_1_button = findViewById(R.id.main_menu_question_1_button_id);
        question_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_menu.this, question_1_view_activity.class);
                startActivity(intent);
            }
        });

        question_2_button = findViewById(R.id.main_menu_question_2_button_id);
        question_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, question_2_view_activity.class);
                startActivity(intent);
            }
        });

        question_3_button = findViewById(R.id.main_menu_question_3_button_id);
        question_3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, question_3_view_activity.class);
                startActivity(intent);
            }
        });
    }
}