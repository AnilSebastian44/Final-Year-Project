package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ManualRecord extends AppCompatActivity {
    EditText record;
    Button record_btn, view_btn;
    DbHelper db;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_record);

        db = new DbHelper(this);
        record = (findViewById(R.id.tv_record));
        view_btn = findViewById(R.id.view_record);
        record_btn = (findViewById(R.id.btn_record));

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = record.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                boolean info = db.checkEmail(userName);
                if (info == true && userName != null && userName.matches(emailPattern)) {
                    //getting the current time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String currentTime = "Registered Time : " + dateFormat.format(calendar.getTime());
                    //time.setText(currentTime);

                    //getting the current date
                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyy");
                    String currentDate = "Registered Date: " + timeFormat.format(calendar.getTime());
                    //date.setText(currentDate);

                    if (userName.length() > 0 && currentDate != null && currentTime != null) {
                        boolean res = db.addUserRecord(userName, currentDate, currentTime);
                        if (res == true) {

                            Toast.makeText(ManualRecord.this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show();

                            view_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent View_attendance = new Intent(ManualRecord.this, ViewAttendance.class);
                                    startActivity(View_attendance);
                                }
                            });
                        }
                    }
                } else
                    Toast.makeText(ManualRecord.this, "User not registered", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
