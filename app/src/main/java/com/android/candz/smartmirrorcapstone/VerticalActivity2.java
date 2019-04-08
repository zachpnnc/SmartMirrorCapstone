package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.core.Data;

public final class VerticalActivity2 extends AppCompatActivity {
    private int callbackId;
    private ListView calendar_event_view;
    private java.util.Calendar calendar;
    private int currentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical2);
        callbackId = 42;

        calendar_event_view = findViewById(R.id.calendar_event_view);

        calendar = java.util.Calendar.getInstance();
        currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR);

        display();
    }

    public String[] convertTimestamp(long startTime, long endTime) {
        Date start = new Date(startTime);
        Date end = new Date(endTime);

        String myStart = DateFormat.getInstance().format(start);
        String myEnd = DateFormat.getInstance().format(end);

        return new String[]{myStart, myEnd};
    }

    /*
     * eventIsCurrentDate
     *
     * Checks the start date of an event.
     *
     * @return true if the event day is the current day.
     */
    public boolean eventIsCurrentDate(Event event) {
        DateTime dateTime = new DateTime(event.dTStart);
        int eventDay = dateTime.getDayOfYear();
        return eventDay == currentDay;
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

        List<EventView> eventViews = new ArrayList<>();
        EventView eventView;
        for (Event event : events) {
            String[] times = convertTimestamp(event.dTStart, event.dTend);
            eventView = new EventView(event.title, event.eventLocation, times);

            // only adds event to the view if the event is for the current day
            if (eventIsCurrentDate(event)) {
                eventViews.add(eventView);
            } else {
                // TODO: display events from nearby dates or display no events
            }
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventViews);
        calendar_event_view.setAdapter(listAdapter);
    }
}
