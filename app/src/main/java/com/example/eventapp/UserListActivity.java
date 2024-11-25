package com.example.eventapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Initialize ListView
        ListView lvUsers = findViewById(R.id.lv_users);

        // Initialize SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        // Fetch the list of users from the SQLite database
        ArrayList<String> users = getAllUsers();

        // Check if the users list is empty or null
        if (users.isEmpty()) {
            Toast.makeText(this, "No users to display", Toast.LENGTH_SHORT).show();
        } else {
            // Set up the ListView with an ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    users
            );
            lvUsers.setAdapter(adapter);
        }
    }

    /**
     * Fetches all users from the SQLite database.
     *
     * @return ArrayList of usernames.
     */
    private ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Open the database
            dbHelper.open();

            // Query the database to fetch all users (assuming getUsers returns a Cursor)
            cursor = dbHelper.getUsers();  // This should return a Cursor object

            // Check if the cursor is not null and contains data
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
