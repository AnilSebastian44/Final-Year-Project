package com.example.login;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewAttendance extends AppCompatActivity {

    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance);


        ListView listView = (ListView) findViewById(R.id.listView);
        db = new DbHelper(this);

        ArrayList<String> list = new ArrayList<>();

        /*     //getting value of user login from MainActivity
        Bundle extras = getIntent().getExtras();
        //final String value = (String) extras.get("User");

        //Cursor data = db.getRecordData(value);*/


        Cursor data = db.getRecordData();

        if (data.getCount() == 0) {
            Toast.makeText(ViewAttendance.this, "No record in the database", Toast.LENGTH_SHORT).show();


        } else {

            while (data.moveToNext()) {

                list.add(data.getString(1));
                list.add(data.getString(2));
                list.add(data.getString(3));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(listAdapter);

            }
        }

    }
}
