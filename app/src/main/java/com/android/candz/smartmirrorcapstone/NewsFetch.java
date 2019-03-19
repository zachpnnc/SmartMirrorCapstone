package com.android.candz.smartmirrorcapstone;

import android.os.StrictMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static java.lang.Thread.sleep;

public class NewsFetch
{

    //    https://newsapi.org/
    private static final String NEWS_API_STARTER = "https://newsapi.org/v2/";
    private static final String NEWS_API_POPULAR_US = "top-headlines?country=us";
    private static final String NEWS_API_API_KEY = "&apiKey=0673baac4f99445aa1d890e7cfe65a7b";
    private static final String NEWS_API_API_KEY_ALONE = "0673baac4f99445aa1d890e7cfe65a7b";
    private static JsonObject jsonObject;
    //Sample API call https://newsapi.org/v2/  0673baac4f99445aa1d890e7cfe65a7b
    //https://newsapi.org/v2/top-headlines?country=us&apiKey=0673baac4f99445aa1d890e7cfe65a7b

    public static String test()
    {

        NewsApiClient newsApiClient = new NewsApiClient(NEWS_API_API_KEY_ALONE);

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("trump")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        System.out.println(response.getArticles().get(0).getTitle());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        return null;
    }


    public static String[][] getNewsHeadlines() throws InterruptedException
    {

//        test();

        String jsonString = openConnectionToNewsAPI_POST();
        if (jsonString == "News Data unable to be retrieved")
        {
            jsonString = openConnectionToNewsAPI_GET();

            while (jsonString == "News Data unable to be retrieved")
            {
                if (jsonString == "News Data unable to be retrieved")
                    jsonString = openConnectionToNewsAPI_POST();
                if (jsonString == "News Data unable to be retrieved")
                    jsonString = openConnectionToNewsAPI_GET();
                if (jsonString == "News Data unable to be retrieved")
                    Thread.sleep(5000);
            }
        }


        jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        //headlineArray[0][index] = headline itself.
        //headlineArray[1][index] = headline Links.
        String[][] headlineArray = new String[2][5];
        headlineArray[0] = parseJsonForHeadlines();
        headlineArray[1] = parseJsonForHeadlineLinks();

        return headlineArray;
    }


    public static String openConnectionToNewsAPI_POST()
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
            InputStream is = connection.getInputStream();
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

    public static String openConnectionToNewsAPI_GET()
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
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //This is where the try will fail if it does fail.
            connection.connect();

//            StringBuffer buffer = new StringBuffer();
            InputStream is = connection.getInputStream();
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

    public static String[] parseJsonForHeadlines()
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

    public static String[] parseJsonForHeadlineLinks()
    {

        String[] headlineLinksArray = new String[5];
        JsonArray jsonArray = jsonObject.getAsJsonArray("articles");
        JsonElement jsonElement;
        JsonObject jsonArrayObject;
        String linkPreTrim;
        for (int i = 0; i < 5; i++)
        {
            jsonElement = jsonArray.get(i);
            jsonArrayObject = (JsonObject) jsonElement;
            linkPreTrim = jsonArrayObject.get("url").toString();
            headlineLinksArray[i] = linkPreTrim.replaceAll("^\"|\"$", "");
        }
        return headlineLinksArray;
    }


}
