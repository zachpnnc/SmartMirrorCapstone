package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;
import static java.security.AccessController.getContext;

public final class VerticalActivity2 extends AppCompatActivity {

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private int callbackId;
    private TextView calendar_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical2);
        callbackId = 42;
        calendar_view = findViewById(R.id.calendar_text);

        addCalendar();
        getDataFromCalendarTable();
    }

    public void addCalendar() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.NAME, "Chris");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "mills@example.com");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.example"); // ACCOUNT_TYPE_LOCAL
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "mills@example.com");
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "CandZ Calendar");
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, "ff0000"); // #?

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, callbackId);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: request permission
        } else {
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            Uri calendarUri = asSyncAdapter(uri, "mills@example.com", "com.example");
            if (getContentResolver() == null) {
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    contentResolver.insert(calendarUri, contentValues);
                } catch(NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR: Cannot insert data.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

    public void getDataFromCalendarTable() {
        String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.OWNER_ACCOUNT
        };

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String[] selectionArgs = new String[] {"" +
                "hera@example.com",     // account name
                "com.example",          // account type
                "hera@example.com"};    // owner account

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, callbackId);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: request permission
        } else {
            // executes query and returns a cursor
            try {
                cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

                if (cur.getCount() <= 0) {
                    Toast.makeText(this, "<= 0", Toast.LENGTH_SHORT).show();
                }

                // cursor iterates through the records
                /*
                while (cur.moveToNext()) {
                    long calID = 0;
                    String displayName = null;
                    String accountName = null;
                    String ownerName = null;

                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                    // TODO: do something with the values
                    //Toast.makeText(this, accountName, Toast.LENGTH_SHORT).show();
                }
                */

                cur.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}