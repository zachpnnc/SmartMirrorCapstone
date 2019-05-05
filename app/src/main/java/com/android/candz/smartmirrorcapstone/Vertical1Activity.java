package com.android.candz.smartmirrorcapstone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.core.Data;


public class Vertical1Activity extends AppCompatActivity
{

    private static final String TAG = "VerticalActivity1";
    private EditText dateText;
    private EditText timeText;
    private Button weatherText;
    private ImageButton weatherIcon;
    private Button[] headlineButtons;

    private static final int CALENDAR_ID = 1;

    private static int buttonSelected;
    private JsonObject jsonWeatherData;




    private static double latitude;
    private static double longitude;

    protected LocationListener locationListener;


    private int iterator;
    boolean run = true; //set it to false if you want to stop the timer
    Handler mHandler = new Handler();

    private int callbackId;
    private ListView calendar_event_view;
    private int currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(TemplateActivity.getTemplate() == 1)
            setContentView(R.layout.activity_vertical1);

        // handles the calendar
        callbackId = 42;

        calendar_event_view = findViewById(R.id.calendar_event_view);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        currentDay = calendar.get(java.util.Calendar.DAY_OF_YEAR);

        handleCalendar();

        //Handles instantiating all of the .XML items.
        handleViews();

        dateText.setText(currentDate());
        setBackgroundColor();
        timer();
        //Prompt user to enter a ZipCode to pull weather information from.

        getGpsCoordinates();

        weatherText.setText(getCurrentWeather());
        weatherText.setTextColor(Color.WHITE);
        handleWeatherButtons();

        //Change the Image that WeatherIcon displays based on the type of weather that is currently
        //occurring.  Values can be obtained from WeatherFetch.fetchCurrentWeatherType()
        //Possible values are Clear sky, few clouds, scattered clouds, broken clouds,
        //shower rain, rain, thunderstorm, snow, mist

        if (confirmWeatherData())
        {
            setWeatherTypeIcon(getCurrentWeatherType());
        }
        handleNewsInformation();

    }

    /*
     * eventIsNearbyDate
     *
     * Checks the start date of an event.
     *
     * @param event - a calendar event
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
     * convertTimestamp
     *
     * Converts a UNIX timestamp for a starting time and an ending
     * time to a simple String date display.
     *
     * @param startTime - the start time of an event as a UNIX timestamp
     * @param endTime - the end time of an event as a UNIX timestamp
     * @return new String[] - returns the starting and ending times
     * in a a String array.
     */
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
     * NOTE: Is not currently used, but is a good method
     *       to have if the specifications change.
     *
     * @param event - a calendar event
     * @return true if the event day is the current day.
     */
    public boolean eventIsCurrentDate(Event event) {
        DateTime dateTime = new DateTime(event.dTStart);
        int eventDay = dateTime.getDayOfYear();
        return eventDay == currentDay;
    }

    /*
     * handleCalendar
     *
     * Handles a device's calendar events by displaying
     * them to the user.
     */
    public void handleCalendar()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, callbackId);
        }

        CalendarProvider calendarProvider = new CalendarProvider(this);
        List<me.everything.providers.android.calendar.Calendar> calendars = calendarProvider.getCalendars().getList();

        // TODO: decide which calendar will be displayed
        me.everything.providers.android.calendar.Calendar mainCalendar = calendars.get(CALENDAR_ID);

        long calendarId = mainCalendar.id;

        Data<Event> eventList = calendarProvider.getEvents(calendarId);
        List<Event> events = eventList.getList();

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
                // TODO: do something?
            }
        }

        // NOTE: events are added to the list in the order of when they were added in the calendar
        calendar_event_view.setAdapter(new CustomCalendarListAdapter(this, eventViews));
    }


    public void handleViews()
    {
        dateText = findViewById(R.id.dateTextV1);
        timeText = findViewById(R.id.timeTextV1);

        weatherText = findViewById(R.id.weatherText1);
        weatherIcon = findViewById(R.id.weatherIcon1);

        //Handles instantiating Headlines and adds them to an array.
        headlineButtons = new Button[5];
        headlineButtons[0] = findViewById(R.id.headline1);
        headlineButtons[1] = findViewById(R.id.headline2);
        headlineButtons[2] = findViewById(R.id.headline3);
        headlineButtons[3] = findViewById(R.id.headline4);
        headlineButtons[4] = findViewById(R.id.headline5);
    }


    public void timer()
    {

        iterator = 1;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                while (run)
                {

                    try
                    {
                        Thread.sleep(1000);
                        mHandler.post(new Runnable()
                        {


                            @Override
                            public void run()
                            {

                                String leadingZero = "0";
                                String secondString;
                                String minuteString;
                                String BLANK = "\u0020";
                                iterator++;
                                Calendar c = Calendar.getInstance();

                                int sec = c.get(Calendar.SECOND);
                                if (sec < 10) secondString = leadingZero + String.valueOf(sec);
                                else secondString = String.valueOf(sec);

                                int min = c.get(Calendar.MINUTE);
                                if (min < 10) minuteString = leadingZero + String.valueOf(min);
                                else minuteString = String.valueOf(min);

                                int hour = c.get(Calendar.HOUR);

                                timeText.setText(BLANK);
                                timeText.getText().clear();


                                timeText.setText(String.valueOf(hour) + ":" + minuteString + ":" + secondString);


                                if (iterator % 1000 == 0)
                                {
                                    updateWeatherData();
                                }

                            }
                        });
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }).start();
    }

    private void setBackgroundColor()
    {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(0x000000);
        weatherText.setBackgroundColor(0x000000);
        weatherIcon.setBackgroundColor(0x000000);
    }

    private void setTextColor(TextView dateText, TextView timeText)
    {
        dateText.setTextColor(Color.WHITE);
        timeText.setTextColor(Color.WHITE);
    }

    private String currentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCurrentWeather()
    {
        //Currently only grabs temperature from Raleigh (RDU).
        //https://w1.weather.gov/xml/current_obs/display.php?stid=KRDU
        //https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
//        WeatherFetch wf = new WeatherFetch();
//        JSONObject json = wf.getJSON(this, zipCode);
//
//
//      WeatherLib is an option to interface with the OpenWeatherMap API
//      http://survivingwithandroid.github.io/WeatherLib/android_weatherlib_start.html
//
//        WeatherConfig config = new WeatherConfig();
//        config.unitSystem = WeatherConfig.UNIT_SYSTEM.I;
//        config.lang = "en";
//        config.ApiKey = "28b3d92963fef3ce6717737997d698b8";
//        config.maxResult = 1;
//        config.numDays = 1;
//
//        try
//        {
//            WeatherClient client = (new WeatherClient.ClientBuilder()).attach(this)
//                    .provider(new OpenweathermapProviderType())
//                    .httpClient(com.survivingwithandroid.weather.lib.client.volley.WeatherClientDefault.class)
//                    .config(config)
//                    .build();
//
//            client.getCurrentCondition();
//        }
//        catch (Throwable t)
//        {
//            t.printStackTrace();
//        } WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();

//        String jsonString = WeatherFetch.getWeatherJsonZip("27616");
        String jsonString = WeatherFetch.getWeatherJsonCoor(longitude, latitude);
        jsonWeatherData = WeatherFetch.convertJsonStringToJsonObject(jsonString);


        //Crashes here
        String temperature = WeatherFetch.fetchCurrentTemperature(jsonWeatherData);
        String degreeSymbol = "\u00b0";
        //Format the string so that only the relevant digits are shown.
        // temperature = String.
        return temperature + degreeSymbol;
    }

    public String getCurrentWeatherType()
    {
        return WeatherFetch.fetchCurrentWeatherType(jsonWeatherData);
    }


    private void setWeatherTypeIcon(String weatherTypeIconId)
    {
        /**
         * previous filename = New filename.
         * R.drawable.fileName won't recognize fileName if filename contains a number.  That is the
         * the reason that the file names were changed.
         *
         * 01d = a
         * 01n = aa
         * 02d = aaa
         * 02n = aaaa
         * 03d = b
         * 03n = bb
         * 04d = bbb
         * 04n = bbbb
         * 09d = c
         * 09n = cc
         * 10d = ccc
         * 10n = cccc
         * 11d = d
         * 11n = dd
         * 13d = ddd
         * 13n = dddd
         * 50d = e
         * 50n = ee
         *
         * 50d and 50n may be a problem with the black background.
         */
        switch (weatherTypeIconId)
        {
            case "01d":
                weatherIcon.setImageResource(R.drawable.a);
                break;
            case "01n":
                weatherIcon.setImageResource(R.drawable.aa);
                break;
            case "02d":
                weatherIcon.setImageResource(R.drawable.aaa);
                break;
            case "02n":
                weatherIcon.setImageResource(R.drawable.aaaa);
                break;
            case "03d":
                weatherIcon.setImageResource(R.drawable.b);
                break;
            case "03n":
                weatherIcon.setImageResource(R.drawable.bb);
                break;
            case "04d":
                weatherIcon.setImageResource(R.drawable.bbb);
                break;
            case "04n":
                weatherIcon.setImageResource(R.drawable.bbbb);
                break;
            case "09d":
                weatherIcon.setImageResource(R.drawable.c);
                break;
            case "09n":
                weatherIcon.setImageResource(R.drawable.cc);
                break;
            case "10d":
                weatherIcon.setImageResource(R.drawable.ccc);
                break;
            case "10n":
                weatherIcon.setImageResource(R.drawable.cccc);
                break;
            case "11d":
                weatherIcon.setImageResource(R.drawable.d);
                break;
            case "11n":
                weatherIcon.setImageResource(R.drawable.dd);
                break;
            case "13d":
                weatherIcon.setImageResource(R.drawable.ddd);
                break;
            case "13n":
                weatherIcon.setImageResource(R.drawable.dddd);
                break;
            case "50d":
                weatherIcon.setImageResource(R.drawable.e);
                break;
            case "50n":
                weatherIcon.setImageResource(R.drawable.ee);
                break;
            default:
                weatherIcon.setImageResource(R.drawable.weather_cloud);
        }
    }

    public boolean confirmWeatherData()
    {
        return WeatherFetch.confirmWeatherData(jsonWeatherData);
    }

    public void updateWeatherData()
    {
        weatherText.setText(getCurrentWeather());
        setWeatherTypeIcon(getCurrentWeatherType());
    }

    public void handleNewsInformation()
    {
//        NewsFetch fetchNews = new NewsFetch();
        final String[][] headlineArray = NewsFetch.getNewsHeadlines();

        if (headlineArray[0][0] == "Currently Rate Limited by NewsAPI")
        {
            for (int i = 0; i < 5; i++)
            {
                headlineButtons[i].setText("Currently Rate Limited by NewsAPI");
            }
        } else
        {
            //Add news headlines to a TextView.
            for (int i = 0; i < 5; i++)
            {
                headlineButtons[i].setText(headlineArray[0][i]);
            }

            headlineButtons[0].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonSelected = 0;
                    Intent weatherExpandedIntent = new Intent(getApplicationContext(), NewsExpanded.class);
                    startActivity(weatherExpandedIntent);
                }
            });

            headlineButtons[1].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonSelected = 1;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlineArray[1][1]));
                    startActivity(browserIntent);
                }
            });

            headlineButtons[2].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonSelected = 2;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlineArray[1][2]));
                    startActivity(browserIntent);
                }
            });

            headlineButtons[3].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonSelected = 3;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlineArray[1][3]));
                    startActivity(browserIntent);
                }
            });

            headlineButtons[4].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    buttonSelected = 4;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(headlineArray[1][4]));
                    startActivity(browserIntent);
                }
            });
        }

    }


    @SuppressLint("MissingPermission")
    public void getGpsCoordinates()
    {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d(TAG,"TEST:" + longitude + " " + latitude);
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d("Latitude", "disable");
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.d("Latitude", "enable");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.d("Latitude", "status");
            }
        };
        try
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        try
        {
            locationListener.onLocationChanged(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Last known location not known!", Toast.LENGTH_SHORT).show();
        }

    }

    public void handleWeatherButtons()
    {
        weatherIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Vertical1Activity.this, WeatherExpanded.class);
                startActivity(intent);
            }
        });

        weatherText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Vertical1Activity.this, WeatherExpanded.class);
                startActivity(intent);
            }
        });
    }

    public static double getLatitude()
    {
        return latitude;
    }

    public static double getLongitude()
    {
        return longitude;
    }

    public static int getButtonSelected()
    {
        return buttonSelected;
    }
}
