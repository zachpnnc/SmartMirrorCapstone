package com.android.candz.smartmirrorcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private EditText usernameText;
    private EditText passwordText;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitButton = findViewById(R.id.button1);
        usernameText = findViewById(R.id.editText1);
        passwordText = findViewById(R.id.editText2);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkLogin(usernameText.getText().toString(), passwordText.getText().toString());
            }
        });
    }

    private void checkLogin(String username, String password)
    {
        if (username.equals("admin") && password.equals("admin"))
        {
            Toast.makeText(getApplicationContext(), "Admin login...", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_template);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid, Try again", Toast.LENGTH_LONG).show();
        }
    }
}
