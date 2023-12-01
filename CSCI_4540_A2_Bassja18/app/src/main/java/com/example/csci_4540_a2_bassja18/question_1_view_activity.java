package com.example.csci_4540_a2_bassja18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class question_1_view_activity extends AppCompatActivity {

    private Button main_menu_button, next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_1_view_activity);

        main_menu_button = findViewById(R.id.question_1_main_menu_button_id);
        main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(question_1_view_activity.this, main_menu.class);
                startActivity(intent);
                finish();
            }
        });

        next_button = findViewById(R.id.question_1_next_button_id);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(question_1_view_activity.this, question_2_view_activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}