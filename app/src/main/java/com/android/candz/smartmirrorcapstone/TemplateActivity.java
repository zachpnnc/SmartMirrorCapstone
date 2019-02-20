package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class TemplateActivity extends AppCompatActivity
{

    private ImageButton template1;
    private ImageButton template2;
    private ImageButton template3;
    private ImageButton template4;
    private ImageButton template5;
    private ImageButton template6;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        template1 = findViewById(R.id.imageButton1);
        template2 = findViewById(R.id.imageButton2);
        template3 = findViewById(R.id.imageButton3);
        template4 = findViewById(R.id.imageButton4);
        template5 = findViewById(R.id.imageButton5);
        template6 = findViewById(R.id.imageButton6);

        template1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Moving to Template 1", Toast.LENGTH_SHORT).show(); // for testing

                //setContentView(R.layout.activity_vertical1);
                // TODO: Fragment layout instead of activity. I'm researching this.
            }
        });

        template2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Moving to Template 2", Toast.LENGTH_SHORT).show();
            }
        });

        template3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Moving to Template 3", Toast.LENGTH_SHORT).show();
            }
        });

        template4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Moving to Template 4", Toast.LENGTH_SHORT).show();
            }
        });

        template5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Moving to Template 5", Toast.LENGTH_SHORT).show();
            }
        });

        template6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Moving to Template 6", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
