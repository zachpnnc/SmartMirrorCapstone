package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements Login {
    private DatabaseHelper userDB;
    private Button registerConfirmBtn;
    private EditText username;
    private EditText password;
    private EditText password_confirm;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDB = new DatabaseHelper(this);
        registerConfirmBtn = findViewById(R.id.register_confirm);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password1);
        password_confirm = findViewById(R.id.password2);
        Button user_info = findViewById(R.id.user_info);
        databaseHelper = new DatabaseHelper(this);

        addData();

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseHelper.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "You must register first!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void addData() {
        registerConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().toLowerCase();
                String pass = password.getText().toString();

                if (checkLogin(user, pass)) {
                    boolean insertData = userDB.addData(user, pass);

                    if (insertData) {
                        Toast.makeText(RegisterActivity.this, "Data saved successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Data was not saved successfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords must match.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // checks to see if the password is valid
    public boolean checkLogin(String username, String password)
    {
        // TODO: password hashing [STRETCH GOAL]

        if (password.equals(password_confirm.getText().toString())) {
            return true;
        }

        return false;
    }
}
