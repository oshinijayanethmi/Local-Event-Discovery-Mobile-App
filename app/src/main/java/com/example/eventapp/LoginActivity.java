package com.example.eventapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private SQLiteHelper dbHelper; // SQLite database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_register);

        // Initialize SQLite helper
        dbHelper = new SQLiteHelper(this);

        // Handle Login button click
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authenticate the user using SQLite
            loginUser(username, password);
        });

        // Handle Register button click
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser(String username, String password) {
        // Check if the username and password are correct in SQLite database
        Cursor cursor = dbHelper.getUserByUsername(username);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password")); // Get the stored password

            if (storedPassword.equals(password)) {
                // Check if the user is an admin or regular user
                if (username.startsWith("admin")) {
                    startActivity(new Intent(LoginActivity.this, AddEventActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                }
                finish(); // Close the login activity
            } else {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            }

            cursor.close(); // Always close the cursor after use
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
