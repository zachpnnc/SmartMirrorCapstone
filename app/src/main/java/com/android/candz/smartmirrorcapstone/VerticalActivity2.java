package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Date;
import org.joda.time.DateTime;
import java.text.DateFormat;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.core.Data;

public final class VerticalActivity2 extends AppCompatActivity {
    private int callbackId;
    private ListView calendar_event_view;
    private int currentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical2);
        callbackId = 42;

        calendar_event_view = findViewById(R.id.calendar_event_view);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
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
     * eventIsNearbyDate
     *
     * Checks the start date of an event.
     *
     * @return true if the event day is, or is nearby the current day
     * within 2 days after the current day.
     */
    public boolean eventIsNearbyDate(Event event) {
        DateTime dateTime = new DateTime(event.dTStart);
        int eventDay = dateTime.getDayOfYear();

        int[] nearbyDates = {currentDay, currentDay + 1, currentDay + 2};

        for (int date : nearbyDates) {
            if (date == eventDay) {
                return true;
            }
        }

        return false;
    }

    /*
     * eventIsCurrentDate
     *
     * Checks the start date of an event.
     *
     * NOTE: Is not currently used, but is a good method
     *       to have if the specifications change.
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

        //List<EventView> eventViews = new ArrayList<>();
        ArrayList<EventView> eventViews = new ArrayList<>();
        EventView eventView;
        for (Event event : events) {
            String[] times = convertTimestamp(event.dTStart, event.dTend);
            eventView = new EventView(event.title, event.eventLocation, times);
            // Only adds event to the view if the event is for the current day
            // or within 2 days after the current day.
            if (eventIsNearbyDate(event)) {
                eventViews.add(eventView);
            } else {
                // do something?
            }
        }

        // NOTE: events are added to the list in the order of when they were added in the calendar
        //ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventViews);
        //calendar_event_view.setAdapter(listAdapter);

        calendar_event_view.setAdapter(new CustomCalendarListAdapter(this, eventViews));
    }
}
