package com.android.candz.smartmirrorcapstone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.content.Context;

public class WeatherFetch
{

    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?zip=";
    private static final String OPEN_WEATHER_MAP_API_KEY = "28b3d92963fef3ce6717737997d698b8";

    public static JSONObject getJSON(Context context, String zipCode)
    {

        try
        {
            String stringURL = OPEN_WEATHER_MAP_API + zipCode + "&APPID=" + OPEN_WEATHER_MAP_API_KEY;
            URL url = new URL(stringURL);

            //EVERYTHING BELOW IS EXPERIMENTAL
            URLConnection connection = url.openConnection();



            //Throws an exception after this line is executed, not sure why.
            // I have tried multiple ways of getting the JSON from OpenWeatherMap and none have
            //worked yet.
            //Result always ends up in the Catch portion of the try catch.
            InputStream inputStream = connection.getInputStream();

            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));


            //Just for testing purposes.
            String test[] = new String[100];
            String currentLine = null;
            int iterator = 0;

            while ((currentLine = input.readLine()) != null)
            {
                test[iterator] = currentLine;
            }


//            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));








            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
            {
                json.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            //Code == "200" means that the attempt to grab data was successful.
            if (data.getInt("cod") != 200)
            {
                return null;
            }

            return data;
        } catch (Exception e)
        {
            return null;
        }
    }

}
