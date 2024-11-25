package com.example.eventapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserDashboardActivity extends AppCompatActivity {
    private SQLiteHelper dbHelper;
    private TextView tvWelcome;
    private EditText etSearch;
    private Button btnSearch, btnLogout;
    private ListView lvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        dbHelper = new SQLiteHelper(this); // Initialize SQLiteHelper

        tvWelcome = findViewById(R.id.tv_welcome_user);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnLogout = findViewById(R.id.btn_logout_user);
        lvEvents = findViewById(R.id.lv_user_events);

        // Set welcome message with user email (assuming Firebase Auth is still used for login)
        String username = "User"; // Retrieve username from Firebase or SQLite
        tvWelcome.setText("Welcome, " + username);

        // Set up the event list view
        loadEvents();

        // Search button click listener
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchEvents(query);
            }
        });

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            // Firebase Auth sign-out (if you still use Firebase Auth)
            // auth.signOut();
            startActivity(new Intent(UserDashboardActivity.this, MainActivity.class));
            finish();
        });
    }

    // Method to load all events from SQLite database and populate the ListView
    private void loadEvents() {
        ArrayList<String> events = dbHelper.getAllEvents();
        if (events.isEmpty()) {
            events.add("No events available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        lvEvents.setAdapter(adapter);

        lvEvents.setOnItemClickListener((parent, view, position, id) -> {
            String eventName = (String) parent.getItemAtPosition(position);
            // You can handle event click here (e.g., show event details)
        });
    }

    // Method to search events in SQLite database based on the query
    private void searchEvents(String query) {
        ArrayList<String> events = dbHelper.searchEvents(query);
        if (events.isEmpty()) {
            events.add("No events found");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        lvEvents.setAdapter(adapter);
    }
}
