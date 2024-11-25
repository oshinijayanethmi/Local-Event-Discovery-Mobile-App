package com.example.eventapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPassword;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnRegister = findViewById(R.id.btn_register);
        TextView tvLogin = findViewById(R.id.tv_login);

        // Initialize SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Open the database
                dbHelper.open();

                // Check if the username already exists
                if (dbHelper.isUsernameTaken(username)) {
                    Toast.makeText(this, "Username already taken!", Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                    return;
                }

                // Register the user by inserting data into SQLite
                if (dbHelper.insertUser(username, password)) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish(); // Close the RegisterActivity
                } else {
                    Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                showErrorSnackbar(e.getMessage());
            } finally {
                // Close the database
                dbHelper.close();
            }
        });

        tvLogin.setOnClickListener(v -> {
            // Go to LoginActivity if the user already has an account
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    /**
     * Show an error Snackbar with a "Copy Error" button.
     *
     * @param errorMessage The error message to display and copy.
     */
    private void showErrorSnackbar(String errorMessage) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Error: " + errorMessage, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Copy Error", v -> {
            // Copy the error message to clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Error", errorMessage);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Error copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        snackbar.show();
    }
}
