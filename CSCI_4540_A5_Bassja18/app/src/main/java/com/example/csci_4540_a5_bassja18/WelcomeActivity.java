package com.example.csci_4540_a5_bassja18;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Retrieve the username from the intent
        String username = getIntent().getStringExtra("username");

        if (username != null) {
            // Display the welcome message
            welcomeTextView.setText("Welcome, " + username);
        }
    }
}
