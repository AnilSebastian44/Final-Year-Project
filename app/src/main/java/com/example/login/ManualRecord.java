package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManualRecord extends AppCompatActivity {
    EditText record;
    Button record_btn, view_btn;
    DbHelper db;
    private String url = "http://192.168.0.129:3000/record_auth";
    private String add_url = "http://192.168.0.129:3000/add_attendance";
    String userName;
    String currentTime;
    String currentDate;
    private String TAG = ManualRecord.class.getName();


    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }*/

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
                userName = record.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                boolean info = db.checkEmail(userName);
                if (userName != null && userName.matches(emailPattern)) {
                    //Validate user email
                    validateUser();

                    //getting the current time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    //Registered Time
                    currentTime = dateFormat.format(calendar.getTime());

                    //getting the current date
                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyy");
                    //Registered Date
                    currentDate = timeFormat.format(calendar.getTime());
                }


                if (userName !=null && currentDate != null && currentTime != null) {

                        addAttendance();

                        //boolean res = db.addUserRecord(userName, currentDate, currentTime);
                        if (true) {

                            //call the record data api

                            Toast.makeText(ManualRecord.this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show();

                            view_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent View_attendance = new Intent(ManualRecord.this, ViewAttendance.class);
                                    startActivity(View_attendance);
                                }
                            });
                        }

                } else
                    Toast.makeText(ManualRecord.this, "User not registered", Toast.LENGTH_SHORT).show();

            }
        });




    }

    public void validateUser(){
        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("userName", userName);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       /* Intent HomePage = new Intent(MainActivity.this, ScanActivity.class);
                        HomePage.putExtra("User", userName);
                        startActivity(HomePage);*/

                        Toast.makeText(ManualRecord.this, "Valid Email", Toast.LENGTH_SHORT).show();


                        Toast.makeText(ManualRecord.this, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(ManualRecord.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                Toast.makeText(ManualRecord.this, error.toString(), Toast.LENGTH_SHORT).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers  = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My user agent");
                return headers;
            }
        };

        //add the request tot he que
        // RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());

        mRequest.add(jsonObjectRequest);

    }


    public void addAttendance(){

        // API call to server to check if user exists

        //queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //passing info
        HashMap<String, String> jsonParams = new HashMap<>();

        jsonParams.put("username", userName);
        jsonParams.put("date", currentDate);
        jsonParams.put("time", currentTime);

        //POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, add_url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ManualRecord.this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show();

                        Toast.makeText(ManualRecord.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Some Error: " + error.getMessage());

                Toast.makeText(ManualRecord.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;


            }
        };

        //add the request to the queue
        // RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

}


