package com.example.csci_4540_a5_bassja18;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.errorTextView);

        // Initialize login and register buttons
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        // Set click listeners for the login and register buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform login action
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform user registration
                registerUser();
            }
        });
    }

    private void loginUser() {
        // Get username and password from the input fields
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Please enter both username and password.");
            return;
        }

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_ID};
        String selection = DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};

        // Query the database to check if the user exists
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // User authenticated
            errorTextView.setText("Login successful!");

            // Start the WelcomeActivity and pass the username
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            // Authentication failed
            errorTextView.setText("Login failed. Please check your credentials.");
        }

        cursor.close();
        db.close();
    }

    private void registerUser() {
        // Get username and password from the input fields
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Please enter both username and password.");
            return;
        }

        // Get a writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insert user data into the database
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);

        if (newRowId > 0) {
            // Registration successful
            errorTextView.setText("Registration successful!");
        } else {
            // Registration failed
            errorTextView.setText("Registration failed. Please try again.");
        }

        db.close();
    }
}
