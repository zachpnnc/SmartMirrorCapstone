package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;

public class WeatherExpanded extends AppCompatActivity
{

    public TextView[] timeArray = new TextView[12];
    public TextView[] weatherArray = new TextView[12];
    public ImageView[] iconArray = new ImageView[12];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);
        instantiateXML();

        String weatherData = WeatherFetch.getAPIData3Hourly();
        JsonObject jsonObject = WeatherFetch.convertJsonStringToJsonObject(weatherData);
        String[] jsonTemperatures = parseJsonForTemperatures(jsonObject);


        String[] adjustedTemperatures = fillInGaps(jsonTemperatures);
        String[] temperatureIcons = parseJsonForIcons(jsonObject);
        //weatherDataArray[0][i] contains temperature data.
        //weatherDataArray[1][i] contains the associated iconId for that hours temperature data;
        String[][] weatherDataArray = new String[2][40];
        weatherDataArray[0] = adjustedTemperatures;
        weatherDataArray[1] = temperatureIcons;

        //Can add in code to load in weather icon, and wind speeds / direction.
        loadXMLinformation(weatherDataArray);
//        WebView test = (WebView) findViewById(R.id.webViewTest);
//
//        String htmlThing = "<div id=\"openweathermap-widget-11\"></div>\n" +
//                "<script src='//openweathermap.org/themes/openweathermap/assets" +
//                "/vendor/owm/js/d3.min.js'></script><script>window.myWidgetParam ?" +
//                " window.myWidgetParam : window.myWidgetParam = [];  window.myWidgetParam.push(" +
//                "{id: 11,cityid: '4487042',appid: '28b3d92963fef3ce6717737997d698b8',units: " +
//                "'imperial',containerid: 'openweathermap-widget-11',  });  (function() {var " +
//                "script = document.createElement('script');script.async = true;script.charset " +
//                "= \"utf-8\";script.src = \"//openweathermap.org/themes/openweathermap/assets/" +
//                "vendor/owm/js/weather-widget-generator.js\";var s = document." +
//                "getElementsByTagName('script')[0];s.parentNode.insertBefore(script, s);  " +
//                "})();</script>";
//
//        WebSettings settings = test.getSettings();
//        settings.setJavaScriptEnabled(true);

    }

    public void instantiateXML()
    {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(0x000000);

        timeArray[0] = findViewById(R.id.textView1);
        timeArray[1] = findViewById(R.id.textView2);
        timeArray[2] = findViewById(R.id.textView3);
        timeArray[3] = findViewById(R.id.textView4);
        timeArray[4] = findViewById(R.id.textView5);
        timeArray[5] = findViewById(R.id.textView6);
        timeArray[6] = findViewById(R.id.textView7);
        timeArray[7] = findViewById(R.id.textView8);
        timeArray[8] = findViewById(R.id.textView9);
        timeArray[9] = findViewById(R.id.textView10);
        timeArray[10] = findViewById(R.id.textView11);
        timeArray[11] = findViewById(R.id.textView12);

        weatherArray[0] = findViewById(R.id.textView13);
        weatherArray[1] = findViewById(R.id.textView14);
        weatherArray[2] = findViewById(R.id.textView15);
        weatherArray[3] = findViewById(R.id.textView16);
        weatherArray[4] = findViewById(R.id.textView17);
        weatherArray[5] = findViewById(R.id.textView18);
        weatherArray[6] = findViewById(R.id.textView19);
        weatherArray[7] = findViewById(R.id.textView20);
        weatherArray[8] = findViewById(R.id.textView21);
        weatherArray[9] = findViewById(R.id.textView22);
        weatherArray[10] = findViewById(R.id.textView23);
        weatherArray[11] = findViewById(R.id.textView24);

        iconArray[0] = findViewById(R.id.iconView1);
        iconArray[1] = findViewById(R.id.iconView2);
        iconArray[2] = findViewById(R.id.iconView3);
        iconArray[3] = findViewById(R.id.iconView4);
        iconArray[4] = findViewById(R.id.iconView5);
        iconArray[5] = findViewById(R.id.iconView6);
        iconArray[6] = findViewById(R.id.iconView7);
        iconArray[7] = findViewById(R.id.iconView8);
        iconArray[8] = findViewById(R.id.iconView9);
        iconArray[9] = findViewById(R.id.iconView10);
        iconArray[10] = findViewById(R.id.iconView11);
        iconArray[11] = findViewById(R.id.iconView12);
    }

    public String[] parseJsonForTemperatures(JsonObject jsonObject)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("list");
        JsonElement element;
        JsonObject object;
        JsonObject test;
        String[] temperatureList = new String[39];
        for (int i = 0; i < 38; i++)
        {
            element = jsonArray.get(i);
            object = (JsonObject) element;
            test = (JsonObject) object.get("main");
            String temp = test.get("temp").toString();

            temperatureList[i] = temp.replaceAll("^\"|\"$", "");
        }
        return temperatureList;
    }

    public String[] parseJsonForIcons(JsonObject jsonObject)
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("list");
//        JsonElement element;
//        JsonObject object;
//        JsonObject test;
        String[] iconList = new String[39];

        //This definitely needs to be tested, don't think it works correctly now.
        for (int i = 0; i < 38; i++)
        {
            JsonElement element = (JsonObject) jsonArray.get(i);
            JsonObject object = (JsonObject) element;
            JsonArray test = (JsonArray) object.getAsJsonArray("weather");
            JsonObject iconObject = (JsonObject) test.get(0);
            JsonElement iconId = iconObject.get("icon");
            String temp = iconId.toString();
            iconList[i] = temp.replaceAll("^\"|\"$", "");
        }
        return iconList;
    }

    public String[] fillInGaps(String[] jsonTemperatures)
    {
        Double[] adjustedTemperatures = new Double[60];
        for (int i = 0; i < 20; i++)
        {
            double temp1 = Double.parseDouble(jsonTemperatures[i]);
            double temp2 = Double.parseDouble(jsonTemperatures[i+1]);
            double differencePerHour = ((temp2 - temp1) / 3);
            adjustedTemperatures[3 * i] = temp1 + differencePerHour;
            adjustedTemperatures[(3 * i) + 1] = adjustedTemperatures[(3 * i)] + differencePerHour;
            adjustedTemperatures[(3 * i) + 2] = adjustedTemperatures[(3 * i) + 1] + differencePerHour;
        }

//        System.out.println(adjustedTemperatures);

        String[] adjustedTemperaturesString = new String[60];
        DecimalFormat df = new DecimalFormat("##");
        for (int i = 0; i < 60; i++)
        {
            adjustedTemperatures[i] = Double.valueOf(df.format(adjustedTemperatures[i]));
            adjustedTemperaturesString[i] = Double.toString(adjustedTemperatures[i]);

            if (adjustedTemperaturesString[i].length() == 4)
            {
                adjustedTemperaturesString[i] = adjustedTemperaturesString[i].substring(0, 2);
            }
            else if (adjustedTemperaturesString[i].length() == 3)
            {
                adjustedTemperaturesString[i] = adjustedTemperaturesString[i].substring(0,1);
            }
        }

        return adjustedTemperaturesString;
    }

    public void loadXMLinformation(String[][] temperatures)
    {
        loadXMLTemperatures(temperatures[0]);
        loadXMLIcons(temperatures[1]);
    }


    public void loadXMLTemperatures(String[] temperatures)
    {
        String degreeSymbol = "\u00b0";

        for (int i = 0; i < 12; i++)
        {
            //Instead of "hours from now" can be changed to actual time.
            //can be difficult to implement.

            timeArray[i].setText(i + " hours from now");
            weatherArray[i].setText(temperatures[i] + degreeSymbol);
//            timeArray[i].setTextColor(0xffffff);
//            weatherArray[i].setTextColor(0xffffff);

        }
    }

    public void loadXMLIcons(String[] icons)
    {
        for (int i = 0; i < 4; i++)
        {
            switch (icons[i])
            {
                case "01d":
                    iconArray[(i*3)].setImageResource(R.drawable.a);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.a);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.a);
                    break;
                case "01n":
                    iconArray[(i*3)].setImageResource(R.drawable.aa);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.aa);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.aa);

                    break;
                case "02d":
                    iconArray[(i*3)].setImageResource(R.drawable.aaa);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.aaa);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.aaa);

                    break;
                case "02n":
                    iconArray[(i*3)].setImageResource(R.drawable.aaaa);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.aaaa);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.aaaa);

                    break;
                case "03d":
                    iconArray[(i*3)].setImageResource(R.drawable.b);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.b);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.b);

                    break;
                case "03n":
                    iconArray[(i*3)].setImageResource(R.drawable.bb);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.bb);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.bb);

                    break;
                case "04d":
                    iconArray[(i*3)].setImageResource(R.drawable.bbb);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.bbb);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.bbb);

                    break;
                case "04n":
                    iconArray[(i*3)].setImageResource(R.drawable.bbbb);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.bbbb);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.bbbb);

                    break;
                case "09d":
                    iconArray[(i*3)].setImageResource(R.drawable.c);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.c);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.c);

                    break;
                case "09n":
                    iconArray[(i*3)].setImageResource(R.drawable.cc);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.cc);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.cc);

                    break;
                case "10d":
                    iconArray[(i*3)].setImageResource(R.drawable.ccc);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.ccc);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.ccc);

                    break;
                case "10n":
                    iconArray[(i*3)].setImageResource(R.drawable.cccc);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.cccc);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.cccc);

                    break;
                case "11d":
                    iconArray[(i*3)].setImageResource(R.drawable.d);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.d);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.d);

                    break;
                case "11n":
                    iconArray[(i*3)].setImageResource(R.drawable.dd);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.dd);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.dd);

                    break;
                case "13d":
                    iconArray[(i*3)].setImageResource(R.drawable.ddd);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.ddd);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.ddd);

                    break;
                case "13n":
                    iconArray[(i*3)].setImageResource(R.drawable.dddd);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.dddd);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.dddd);

                    break;
                case "50d":
                    iconArray[(i*3)].setImageResource(R.drawable.e);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.e);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.e);

                    break;
                case "50n":
                    iconArray[(i*3)].setImageResource(R.drawable.ee);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.ee);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.ee);
                    break;
                default:
                    iconArray[i].setImageResource(R.drawable.weather_cloud);
                    iconArray[(i*3) + 1].setImageResource(R.drawable.weather_cloud);
                    iconArray[(i*3) + 2].setImageResource(R.drawable.weather_cloud);

            }
        }
    }
}
