package com.example.eventapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EventsDB";
    private static final int DATABASE_VERSION = 1;

    // Table and column names for events
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "id";
    private static final String COLUMN_EVENT_NAME = "name";

    // Table and column names for users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // role can be 'user' or 'admin'

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create events table
        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_NAME + " TEXT NOT NULL)";
        db.execSQL(createEventsTable);

        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_ROLE + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Add an event
    public boolean addEvent(String eventName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, eventName);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return result != -1;  // Return true if insertion is successful
    }

    // Get all events
    public ArrayList<String> getAllEvents() {
        ArrayList<String> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EVENTS, new String[]{COLUMN_EVENT_NAME},
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    events.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Delete all events
    public void clearEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, null, null);
        db.close();
    }

    // Get event by ID
    public Cursor getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EVENTS, null, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)}, null, null, null);
    }

    // Get user by username
    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }

    // Add a user to the database
    public boolean addUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role); // role could be 'user' or 'admin'

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // Return true if insertion is successful
    }

    // Update user password (if needed)
    public boolean updateUserPassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsUpdated > 0;  // Return true if any row is updated
    }

    // Check if a username already exists
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Search events by name
    public ArrayList<String> searchEvents(String query) {
        ArrayList<String> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Use SQL query with WHERE clause to filter events based on the query
            cursor = db.query(TABLE_EVENTS, new String[]{COLUMN_EVENT_NAME},
                    COLUMN_EVENT_NAME + " LIKE ?", new String[]{"%" + query + "%"},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    events.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return events;
    }

    // Open the database (not needed in this case, as SQLiteOpenHelper manages the opening/closing of the database)
    public void open() {
        // Open database logic, but it’s handled by SQLiteOpenHelper
    }

    // Insert a user (alternative to addUser method, for simplicity)
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, "user"); // Default role is 'user'

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // Return true if insertion is successful
    }

    public Cursor getUsers() {
        return null;
    }
}
