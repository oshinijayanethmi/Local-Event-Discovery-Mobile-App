package com.example.eventapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity {

    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize database helper
        dbHelper = new SQLiteHelper(this);

        // Initialize UI elements
        Button btnViewUsers = findViewById(R.id.btn_view_users);
        Button btnManageEvents = findViewById(R.id.btn_manage_events);
        Button btnLogout = findViewById(R.id.btn_logout);

        // View Users button action
        btnViewUsers.setOnClickListener(v -> {
            ArrayList<String> users = getAllUsers();
            if (users.isEmpty()) {
                Toast.makeText(AdminDashboard.this, "No users found!", Toast.LENGTH_SHORT).show();
            } else {
                // You can display users in a list or pass them to another activity
                Intent intent = new Intent(AdminDashboard.this, UserListActivity.class);
                intent.putStringArrayListExtra("users", users);
                startActivity(intent);
            }
        });

        // Manage Events button action
        btnManageEvents.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, EventManagementActivity.class);
            startActivity(intent);
        });

        // Logout button action
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(AdminDashboard.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
        });
    }

    /**
     * Fetches all users from the database.
     *
     * @return ArrayList of usernames.
     */
    private ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Open the database
            dbHelper.open();

            // Fetch all users from the database
            cursor = dbHelper.getUsers();  // Assuming getUsers() returns a Cursor object

            // Check if the cursor contains data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract the username from the cursor
                    @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                    users.add(username); // Add username to the list
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error fetching users", Toast.LENGTH_SHORT).show();
        } finally {
            // Always close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            dbHelper.close();
        }
        return users;
    }
}
