package com.example.eventapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventName;
    private Button btnSaveEvent;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        etEventName = findViewById(R.id.et_event_name);
        btnSaveEvent = findViewById(R.id.btn_save_event);

        // Initialize SQLiteHelper (database helper)
        dbHelper = new SQLiteHelper(this);

        // Set up the save button's click listener
        btnSaveEvent.setOnClickListener(v -> {
            String eventName = etEventName.getText().toString().trim();

            if (!eventName.isEmpty()) {
                // Try adding the event to the database
                boolean isEventAdded = dbHelper.addEvent(eventName);

                if (isEventAdded) {
                    // If the event is added successfully, show a success message and close the activity
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and go back to AdminDashboardActivity
                } else {
                    // If the event could not be added, show an error message
                    Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If the event name is empty, show a warning message
                Toast.makeText(AddEventActivity.this, "Event name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
