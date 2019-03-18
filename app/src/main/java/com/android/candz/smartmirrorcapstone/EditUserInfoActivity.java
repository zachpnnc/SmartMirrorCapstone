package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserInfoActivity extends AppCompatActivity {
    private Button delete;
    private Button save;
    private EditText edit_name;

    private String name;
    private int id;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        delete = findViewById(R.id.edit_delete);
        save = findViewById(R.id.edit_save);
        edit_name = findViewById(R.id.edit_name);

        databaseHelper = new DatabaseHelper(this);

        // gets the intent extras from UserInfoActivity
        Intent gotIntent = getIntent();
        id = gotIntent.getIntExtra("id", -1);
        name = gotIntent.getStringExtra("name");

        edit_name.setText(name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edit_name.getText().toString();
                if (newName.equals("")) {
                    Toast.makeText(EditUserInfoActivity.this, "Please enter a name.", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.updateName(name, newName, id);
                    Toast.makeText(EditUserInfoActivity.this, "Data saved successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteUser(name, id);
                edit_name.setText("");
                Toast.makeText(EditUserInfoActivity.this, "User deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
