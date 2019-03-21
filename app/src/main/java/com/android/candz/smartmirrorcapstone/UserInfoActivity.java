package com.android.candz.smartmirrorcapstone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    private ListView lv;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        lv = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        addDataToList();
    }

    public void addDataToList() {
        Cursor data = databaseHelper.getData();

        ArrayList<String> list = new ArrayList<>();
        while (data.moveToNext()) {
            list.add(data.getString(1)); // 1 refers to COL1 (username)
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();

                Cursor data = databaseHelper.getItemID(name);

                int id = -1;
                while (data.moveToNext()) {
                    id = data.getInt(0);
                }

                if (id <= -1) {
                    Toast.makeText(UserInfoActivity.this, "There is no such user", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editUserIntent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
                    editUserIntent.putExtra("id", id).putExtra("name", name);
                    startActivity(editUserIntent);
                }
            }
        });
    }
}
