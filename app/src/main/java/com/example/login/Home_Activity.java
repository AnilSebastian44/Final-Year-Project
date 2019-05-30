package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home_Activity extends AppCompatActivity {


    Button student;
    Button lecturer;
    Button admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        student = findViewById(R.id.btn_student);
        lecturer = findViewById(R.id.btn_lecturer);
        admin = findViewById(R.id.btn_admin);


        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Home_Activity.this, MainActivity.class);
                //HomePage.putExtra("User",userName);
                startActivity(login);

            }
        });

        lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Home_Activity.this, Lecturer_login.class);
                //HomePage.putExtra("User",userName);
                startActivity(login);

            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Home_Activity.this, MainActivity.class);
                //HomePage.putExtra("User",userName);
                startActivity(login);

            }
        });

    }
}
