package com.example.eventapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EventDetailsActivity extends AppCompatActivity {
    private TextView tvTitle, tvDescription, tvCategory, tvContact, tvDate, tvTime, tvLocation;
    private Button btnBookTickets, btnViewLocation;

    private SQLiteHelper dbHelper; // SQLite database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize UI components
        tvTitle = findViewById(R.id.tv_event_title);
        tvDescription = findViewById(R.id.tv_event_description);
        tvCategory = findViewById(R.id.tv_event_category);
        tvContact = findViewById(R.id.tv_event_contact);
        tvDate = findViewById(R.id.tv_event_date);
        tvTime = findViewById(R.id.tv_event_time);
        tvLocation = findViewById(R.id.tv_event_location);
        btnBookTickets = findViewById(R.id.btn_book_tickets);
        btnViewLocation = findViewById(R.id.btn_view_location);

        // Initialize SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        // Retrieve event ID from the intent
        int eventId = getIntent().getIntExtra("eventId", -1);

        // Load event details from the database
        if (eventId != -1) {
            loadEventDetails(eventId);
        }

        // Handle Book Tickets button click
        btnBookTickets.setOnClickListener(v -> {
            // Logic for booking tickets (e.g., navigating to a booking page)
            // You could start a new Activity or implement booking logic here.
        });

        // Handle View Location button click
        btnViewLocation.setOnClickListener(v -> {
            String location = tvLocation.getText().toString().trim();
            if (!location.isEmpty()) {
                String geoUri = "geo:0,0?q=" + Uri.encode(location);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            } else {
                // Handle case if location is empty
            }
        });
    }

    private void loadEventDetails(int eventId) {
        // Query the database for event details
        Cursor cursor = dbHelper.getEventById(eventId);

        if (cursor != null && cursor.moveToFirst()) {
            // Populate UI fields with event data
            tvTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
            tvDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
            tvCategory.setText(cursor.getString(cursor.getColumnIndex("category")));
            tvContact.setText(cursor.getString(cursor.getColumnIndex("contact")));
            tvDate.setText(cursor.getString(cursor.getColumnIndex("date")));
            tvTime.setText(cursor.getString(cursor.getColumnIndex("time")));
            tvLocation.setText(cursor.getString(cursor.getColumnIndex("location")));

            cursor.close(); // Close the cursor to release resources
        } else {
            // Handle case when cursor is null (i.e., no event found)
            // Show a message or empty state in UI
        }
    }
}
