package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Login
{
    private EditText usernameText;
    private EditText passwordText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitButton = findViewById(R.id.button1);
        Button registerButton = findViewById(R.id.register);
        usernameText = findViewById(R.id.editText1);
        passwordText = findViewById(R.id.editText2);

        databaseHelper = new DatabaseHelper(this);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkLogin(usernameText.getText().toString().toLowerCase(), passwordText.getText().toString()))
                {
                    Intent intent = new Intent(getApplicationContext(), TemplateActivity.class);
                    startActivity(intent);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkLogin(String username, String password)
    {
        String name = username.toLowerCase();
        String message = "You must enter in a valid username and password.\n" +
                "Or, select a valid user from the User Info page and enter in the correct password";

        if (username.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            return false;
        }

        // checks for valid username and valid password
        if (databaseHelper.searchName(name) && databaseHelper.searchPassword(password)) {
            Cursor cursorID = databaseHelper.getItemID(name);
            if (cursorID.moveToFirst()) {
                int id = cursorID.getInt(0); // if int

                // checks for a valid user profile (username and password match)
                if (databaseHelper.checkLogin(id)) {
                    return true;
                }
            }
        }
        return false;

    }
}
