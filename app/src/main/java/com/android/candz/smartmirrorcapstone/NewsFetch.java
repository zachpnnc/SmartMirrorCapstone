package com.android.candz.smartmirrorcapstone;

import android.os.StrictMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NewsFetch
{
//    https://newsapi.org/
    private static final String NEWS_API_STARTER = "https://newsapi.org/v2/";
    private static final String NEWS_API_POPULAR_US = "top-headlines?country=us";
    private static final String NEWS_API_API_KEY = "&apiKey=0673baac4f99445aa1d890e7cfe65a7b";
    //Sample API call https://newsapi.org/v2/  0673baac4f99445aa1d890e7cfe65a7b
    //https://newsapi.org/v2/top-headlines?country=us&apiKey=0673baac4f99445aa1d890e7cfe65a7b


    public static String[] getNewsHeadlines()
    {
        String jsonString = openConnectionToNewsAPI();
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        String[] headLineArray = new String[5];
        headLineArray = parseJsonForHeadlines(jsonObject);

        return headLineArray;
    }


    public static String openConnectionToNewsAPI()
    {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        HttpURLConnection connection = null;
//        InputStream inputStream = null;

        try
        {
            String urlString = NEWS_API_STARTER + NEWS_API_POPULAR_US + NEWS_API_API_KEY;
            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //This is where the try will fail if it does fail.
            connection.connect();

//            StringBuffer buffer = new StringBuffer();
            InputStream is =  connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            is.close();
            connection.disconnect();
            return line;

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
//        finally
//        {
//            try
//            {
//                is.close();
//            }
//            catch (Throwable t)
//            {
//            }
//            try
//            {
//                connection.disconnect();
//            }
//            catch (Throwable t)
//            {
//            }
//        }

        return "News Data unable to be retrieved";
    }

    public static String[] parseJsonForHeadlines(JsonObject jsonObject)
    {
        String[] headlinesArray = new String[5];
        JsonArray jsonArray = jsonObject.getAsJsonArray("articles");
        JsonElement jsonElement;
        JsonObject jsonArrayObject;

        for (int i = 0; i < 5; i++)
        {
            jsonElement = jsonArray.get(i);
            jsonArrayObject = (JsonObject) jsonElement;
            headlinesArray[i] = jsonArrayObject.get("title").toString();
        }

        return headlinesArray;
    }




}
