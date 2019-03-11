package com.android.candz.smartmirrorcapstone;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Vertical1Activity extends AppCompatActivity
{

    private TextView dateText;
    private TextView timeText;
    private TextView weatherText;
    private ImageView weatherIcon;
    boolean run = true; //set it to false if you want to stop the timer
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical1);

        dateText = findViewById(R.id.dateTextV1);
        timeText = findViewById(R.id.timeTextV1);
        weatherText = findViewById(R.id.weatherText1);


        dateText.setText(currentDate());
        setBackgroundColor();
        setTextColor(dateText, timeText);
        timer();

        //Determine zip Code of user

        weatherText.setText(getCurrentWeather("27616"));
        weatherText.setTextColor(Color.WHITE);
    }


    public void timer()
    {
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
                                Calendar c = Calendar.getInstance();
                                int sec = c.get(Calendar.SECOND);
                                int min = c.get(Calendar.MINUTE);
                                int hour = c.get(Calendar.HOUR);
                                timeText.setText(String.valueOf(hour) + ":" +
                                        String.valueOf(min) + ":" + String.valueOf(sec));
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

    public String getCurrentWeather(String zipCode)
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

        String jsonString = WeatherFetch.getWeatherJsonZip("27616");

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        //Crashes here
        String temperature = jsonObject.getAsJsonObject("main").get("temp").toString();

        return temperature;
    }

}
