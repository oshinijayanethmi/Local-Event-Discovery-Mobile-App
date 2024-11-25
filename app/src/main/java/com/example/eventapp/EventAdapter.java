package com.example.eventapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> events;

    public EventAdapter(Context context, ArrayList<String> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size(); // Number of events
    }

    @Override
    public Object getItem(int position) {
        return events.get(position); // Event at the given position
    }

    @Override
    public long getItemId(int position) {
        return position; // Position as the unique ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the custom layout for each list item
            convertView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        }

        // Bind the data
        TextView tvEventName = convertView.findViewById(R.id.tv_event_title);
        tvEventName.setText(events.get(position));

        return convertView;
    }
}
