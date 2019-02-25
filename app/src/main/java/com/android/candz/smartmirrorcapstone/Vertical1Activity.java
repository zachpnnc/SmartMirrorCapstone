package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Vertical1Activity extends AppCompatActivity
{

    private EditText dateText;
    private EditText timeText;
//    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical1);

        dateText = findViewById(R.id.dateTextV1);
        timeText = findViewById(R.id.timeTextV1);
        timeText.setText("Test");
    }
}
