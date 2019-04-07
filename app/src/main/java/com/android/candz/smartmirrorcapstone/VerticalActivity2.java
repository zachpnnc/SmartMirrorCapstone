package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.core.Data;

public final class VerticalActivity2 extends AppCompatActivity {
    private int callbackId;
    private ListView calendar_event_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical2);
        callbackId = 42;

        calendar_event_view = findViewById(R.id.calendar_event_view);

        display();
    }

    public void display() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, callbackId);
        }

        CalendarProvider calendarProvider = new CalendarProvider(this);
        List<Calendar> calendars = calendarProvider.getCalendars().getList();

        // TODO: decide which calendar will be displayed
        Calendar mainCalendar = calendars.get(1);

        long calendarId = mainCalendar.id;

        Data<Event> eventList = calendarProvider.getEvents(calendarId);
        List<Event> events = eventList.getList();

        List<String> eventTitles = new ArrayList<>();
        for (Event event : events) {
            eventTitles.add(event.title);
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventTitles);
        calendar_event_view.setAdapter(listAdapter);
    }
}
