package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper userDB;
    private Button registerConfirmBtn;
    private EditText username;
    private EditText password;
    private EditText password_confirm;
    private EditText email;
    private Button user_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDB = new DatabaseHelper(this);
        registerConfirmBtn = findViewById(R.id.register_confirm);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password1);
        password_confirm = findViewById(R.id.password2);
        email = findViewById(R.id.email);
        user_info = findViewById(R.id.user_info);

        addData();

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addData() {
        registerConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean insertData = userDB.addData(username.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString());

                if (insertData == true) {
                    Toast.makeText(RegisterActivity.this, "Data insert successful.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Data was not inserted successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // TODO: Could definitely make this into an abstract method
    //       that gets implemented here.
    //       Or, we could have an abstract class or interface.
    private boolean checkLogin(String username, String password)
    {
        if (password.equals(password_confirm)) {
            return true;
        }
        return false;
    }
}
