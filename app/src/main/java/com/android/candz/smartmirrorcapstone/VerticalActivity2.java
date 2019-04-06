package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.core.Data;

public final class VerticalActivity2 extends AppCompatActivity {
    private int callbackId;
    //private LinearLayout ll;

    private TextView accountView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical2);
        callbackId = 42;

        accountView = findViewById(R.id.accountName);
        //ll = findViewById(R.id.ll);

        display();
    }

    public void display() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, callbackId);
        }

        CalendarProvider calendarProvider = new CalendarProvider(this);
        List<Calendar> calendars = calendarProvider.getCalendars().getList();

        Calendar mainCalendar = calendars.get(1);
        //TextView tv1 =  new TextView(this);

        String displayName = mainCalendar.displayName;
        long calendarId = mainCalendar.id;
        accountView.setText(displayName);

        Data<Event> eventList = calendarProvider.getEvents(calendarId);
        List<Event> events = eventList.getList();
        for (int i = 0; i < events.size(); i++) {
            accountView.append(events.get(i).eventLocation);
            Toast.makeText(getApplicationContext(), events.get(i).title, Toast.LENGTH_SHORT).show(); // for testing
        }
    }
}
