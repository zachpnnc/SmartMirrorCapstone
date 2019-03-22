package com.android.candz.smartmirrorcapstone;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class WeatherFetch extends Activity
{

    private static final String OPEN_WEATHER_MAP_API_ZIP = "http://api.openweathermap.org/data/2.5/weather?zip=";
    private static final String OPEN_WEATHER_MAP_API_LO_LAT = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String OPEN_WEATHER_MAP_API_KEY = "28b3d92963fef3ce6717737997d698b8";
    private static final String OPEN_WEATHER_MAP_API_METRIC = "&units=imperial&APPID=";

    public static JSONObject getJSON(Context context, String zipCode)
    {

        try
        {
            String stringURL = OPEN_WEATHER_MAP_API_ZIP + zipCode + "&APPID=" + OPEN_WEATHER_MAP_API_KEY;
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
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getWeatherJsonZip(String zipCode)
    {
        /**THIS CODE IS VERY DANGEROUS.  POOR INTERNET WILL CAUSE THE APP TO CRASH **/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /**THIS CODE IS VERY DANGEROUS.  POOR INTERNET WILL CAUSE THE APP TO CRASH **/


        HttpURLConnection con = null;
        InputStream is = null;

        try
        {
            URL url = new URL(OPEN_WEATHER_MAP_API_ZIP + zipCode + OPEN_WEATHER_MAP_API_METRIC + OPEN_WEATHER_MAP_API_KEY);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);

            //This is where the try will fail if it does fail.
            con.connect();

//            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
//            while ((line = br.readLine()) != null)
//                buffer.append(line + "");
            is.close();

            con.disconnect();
//            return buffer.toString();
            return line;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        } finally
        {
            try
            {
                is.close();
            }
            catch (Throwable t)
            {
            }
            try
            {
                con.disconnect();
            }
            catch (Throwable t)
            {
            }
        }

        return "Weather Data unable to be retrieved";
    }

    public static String getWeatherJsonCoor(double lo, double la)
    {
        /**THIS CODE IS VERY DANGEROUS.  POOR INTERNET WILL CAUSE THE APP TO CRASH **/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /**THIS CODE IS VERY DANGEROUS.  POOR INTERNET WILL CAUSE THE APP TO CRASH **/

        DecimalFormat df = new DecimalFormat("###.#####");
        lo = Double.valueOf(df.format(lo));
        la = Double.valueOf(df.format(la));


        HttpURLConnection con = null;
        InputStream is = null;
        String openWeatherMapLongLat;
        openWeatherMapLongLat = "lat=" + la + "&lon=" + lo;

        try
        {
            String urlString = OPEN_WEATHER_MAP_API_LO_LAT + openWeatherMapLongLat + OPEN_WEATHER_MAP_API_METRIC + OPEN_WEATHER_MAP_API_KEY;
            URL url = new URL(urlString);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);

            //This is where the try will fail if it does fail.
            con.connect();

//            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
//            while ((line = br.readLine()) != null)
//                buffer.append(line + "");
            is.close();

            con.disconnect();
//            return buffer.toString();
            return line;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        } finally
        {
            try
            {
                is.close();
            }
            catch (Throwable t)
            {
            }
            try
            {
                con.disconnect();
            }
            catch (Throwable t)
            {
            }
        }
        return "Weather Data unable to be retrieved";
    }

    public static JsonObject convertJsonStringToJsonObject(String jsonString)
    {
        return new JsonParser().parse(jsonString).getAsJsonObject();
    }

    public static String fetchCurrentTemperature(JsonObject weatherJson)
    {
        String unformattedTemp = weatherJson.getAsJsonObject("main").get("temp").toString();
        int length = unformattedTemp.length();
        String formattedTemp = unformattedTemp.substring(0, length - 3);
        return formattedTemp;
    }

    public static String fetchCurrentWeatherType(JsonObject weatherJson)
    {
        //Takes the weatherJson and grabs the weather Array that it contains.
        JsonArray array = weatherJson.getAsJsonArray("weather");
        //Converts the 0th element of the array into an element.
        JsonElement JsonArrayElement = array.get(0);
        //Converts the element into an object.
        JsonObject JsonArrayElementObject = (JsonObject) JsonArrayElement;
        //Converts the "icon" value into a String value and returns that.
        String iconId = JsonArrayElementObject.get("icon").toString();

        //Strips iconId of the quotes that it gets from the Json.
        iconId = iconId.replaceAll("^\"|\"$", "");

        return iconId;
    }

    public static boolean confirmWeatherData(JsonObject weatherData)
    {

        String weatherConditionCode = weatherData.get("cod").toString();
        if (weatherConditionCode.equals("200"))
        {
            return true;
        }
        return false;
    }


}