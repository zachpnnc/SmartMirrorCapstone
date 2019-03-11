package com.android.candz.smartmirrorcapstone;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
    }
}
