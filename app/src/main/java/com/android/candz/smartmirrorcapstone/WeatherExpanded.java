package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WeatherExpanded extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        String weatherData = WeatherFetch.getAPIData3Hourly();
        JsonObject jsonObject = WeatherFetch.convertJsonStringToJsonObject(weatherData);
        String[] jsonTemperatures = parseJsonForTemperatures(jsonObject);
    }

    public String[] parseJsonForTemperatures(JsonObject jsonObject)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("list");
        JsonElement element;
        JsonObject object;
        JsonObject test;
        String[] temperatureList = new String[40];
        for (int i = 0; i < 40; i++)
        {
            element = jsonArray.get(i);
            object = (JsonObject) element;
            test = (JsonObject) object.get("main");
            temperatureList[i] = test.get("temp").toString();
        }
        return temperatureList;
    }
}
