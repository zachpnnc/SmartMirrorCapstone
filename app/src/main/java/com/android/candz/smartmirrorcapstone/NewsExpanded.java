package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NewsExpanded extends AppCompatActivity
{
    private String[][] headlineArray;
    private TextView headline;
    private TextView description;
    private TextView content;
    private ImageView headlineImage;
    private JsonObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        headlineArray = NewsFetch.getHeadlineArray();
        int headlineSelected = Vertical1Activity.getButtonSelected();
        jsonObject = NewsFetch.getJsonObject();


        headline = findViewById(R.id.weatherTextView1);
        description = findViewById(R.id.weatherTextView2);
        content = findViewById(R.id.weatherTextView3);

        headline.setText(headlineArray[0][headlineSelected]);
        description.setText(getDescriptionFromJson());
        content.setText(getContentFromJson());


    }

    public String getDescriptionFromJson()
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("articles");
        JsonElement jsonElement;
        JsonObject jsonArrayObject;
        String linkPreTrim;
        jsonElement = jsonArray.get(Vertical1Activity.getButtonSelected());
        jsonArrayObject = (JsonObject) jsonElement;
        linkPreTrim = jsonArrayObject.get("description").toString();
        return linkPreTrim.replaceAll("^\"|\"$", "");
    }

    public String getContentFromJson()
    {
        JsonArray jsonArray = jsonObject.getAsJsonArray("articles");
        JsonElement jsonElement;
        JsonObject jsonArrayObject;
        String linkPreTrim;
        jsonElement = jsonArray.get(Vertical1Activity.getButtonSelected());
        jsonArrayObject = (JsonObject) jsonElement;
        linkPreTrim = jsonArrayObject.get("content").toString();
        return linkPreTrim.replaceAll("^\"|\"$", "");
    }
}
