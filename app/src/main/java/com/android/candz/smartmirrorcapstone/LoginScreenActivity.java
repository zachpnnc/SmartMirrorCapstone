package com.android.candz.smartmirrorcapstone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreenActivity extends Activity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button submitButton = (Button) findViewById(R.id.button1);
        final EditText usernameText = (EditText) findViewById(R.id.editText1);
        final EditText passwordText = (EditText) findViewById(R.id.editText2);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (usernameText.getText().toString().equals("admin") &&
                passwordText.getText().toString().equals("admin"))
                {
                    Toast.makeText(getApplicationContext(), "Valid, Redirecting...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid, try again", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
