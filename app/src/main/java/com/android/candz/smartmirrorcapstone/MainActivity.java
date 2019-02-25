package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                if (checkLogin(usernameText.getText().toString(), passwordText.getText().toString()))
                {
                    Intent intent = new Intent(getApplicationContext(), TemplateActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkLogin(String username, String password)
    {
        // TODO: will mess with this later
        if ((username.equals("admin") && password.equals("admin")) ||
                (username.equals("") && password.equals("")))
        {
            return true;
        }
        return false;
    }
}
