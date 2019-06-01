package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mobi.inthepocket.android.beacons.ibeaconscanner.Beacon;
import mobi.inthepocket.android.beacons.ibeaconscanner.Error;
import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;

public class ScanActivity extends AppCompatActivity implements IBeaconScanner.Callback {

    private static final String TAG = "ScanActivity";
    TextView uuid, matching, user, date, time, notification;
    Button view_btn, add_attendance, logout;
    String uuid_match = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    Integer greenMajor = 47505;
    String currentDate;
    String currentTime;
    String user_value;

    /**
     * For Home network
     */
    private String url = "http://192.168.0.10:3000/add_attendance";

    /**
     * For College Guest Wifi network
     */
    //private String url = "http://172.19.1.233:3000/add_atendance";

    SharedPreferences sharedPreferences;

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        // initialize
        IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
        IBeaconScanner.getInstance().setCallback(this);

        uuid = findViewById(R.id.tv_uuid);
        matching = findViewById(R.id.tv_uuid_match);
        user = findViewById(R.id.tv_user);
        date = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);
        notification = findViewById(R.id.tv_notification);


        //attendance view button
        view_btn = findViewById(R.id.view_record);
/*
        add_attendance = findViewById(R.id.btn_add);
        add_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_attendance = new Intent(ScanActivity.this, ManualRecord.class);
                startActivity(add_attendance);

            }
        });*/

        /*AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(user_value)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Attendance")
                .setMessage("Your attendance is being recorded")
                .create();
        myAlert.show();*/

//sharedPreferences= getSharedPreferences("loginPref",MODE_PRIVATE);
        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();*/

                Intent Homepage = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(Homepage);

            }
        });

    }


    @Override
    protected void onResume() {


        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            //Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            //Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Start monitoring");

            //green beacon
            Beacon beacon = Beacon.newBuilder()
                    .setUUID("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                    .setMajor(47505)
                    .setMinor(38417)
                    .build();
            IBeaconScanner.getInstance().startMonitoring(beacon);

            //Ice Blue beacon
            Beacon beacon1 = Beacon.newBuilder()
                    .setUUID("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                    .setMajor(21443)
                    .setMinor(21804)
                    .build();
            IBeaconScanner.getInstance().startMonitoring(beacon1);

            //Dark Blue beacon
            Beacon beacon2 = Beacon.newBuilder()
                    .setUUID("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                    .setMajor(25871)
                    .setMinor(64122)
                    .build();
            IBeaconScanner.getInstance().startMonitoring(beacon2);

        }
        super.onResume();
    }

    @Override
    public void onStop() {


        super.onStop();
    }


    @Override
    public void didEnterBeacon(Beacon beacon) {
        Log.d("app", "Entered beacon with UUID " + beacon.getUUID() + beacon.getMinor() + beacon.getMajor());

       // Toast.makeText(ScanActivity.this, beacon.getUUID() + "Found assigned beacons UUID", Toast.LENGTH_SHORT).show();


        //checking if the UUID found is the right beacon.
        //Mint Green beacon class 1
        if (beacon.getUUID().toString().equals(uuid_match) && beacon.getMajor() == greenMajor) {

            //setting text
            uuid.setText("UUID: " + beacon.getUUID().toString());

            matching.setText("UUID IS MATCHING");


            //getting value of user login from MainActivity
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                user_value = (String) extras.get("User");
                user.setText(user_value);


                //getting the current time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                //Registered Time
                currentTime = dateFormat.format(calendar.getTime());
                time.setText(currentTime);

                //getting the current date
                SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyy");
                //Registered Date
                currentDate = timeFormat.format(calendar.getTime());
                date.setText(currentDate);


                //adding username, date and time to the database table RecordUsers

                if (user_value != null && currentDate != null && currentTime != null) {

                    /**
                     * Calling the add attendance function
                     */
                    addAttendance();

                    /**
                     * Giving user feedback on their attendance being recorded
                    */
                    AlertDialog.Builder Alert = new AlertDialog.Builder(this);
                    Alert.setMessage("Attendance")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Attendance for: " + user_value)
                            .setMessage("Recorded at: " + currentTime + "\n"+ "\n"
                                    + "On the: " + currentDate + "\n"+ "\n"
                                    + "For the class: " + "Some class")
                            .create();
                    Alert.show();


                    view_btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent View_attandance = new Intent(ScanActivity.this, ViewAttendance.class);
                            View_attandance.putExtra("User", user_value);
                            startActivity(View_attandance);
                        }
                    });

                    // boolean res = db.addUserRecord(user_value, currentDate, currentTime);
      /*              if (res == true) {

                        Toast.makeText(ScanActivity.this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show();
                        view_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent View_attandance = new Intent(ScanActivity.this, ViewAttendance.class);
                                View_attandance.putExtra("User", value);
                                startActivity(View_attandance);
                            }
                        });
                    }// end of if

    */
                }

                //record attandance

            } else {
                matching.setText("UUID NOT MATCHING");
                Toast.makeText(ScanActivity.this, "Beacon UUID not matching", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void addAttendance() {

        /**
         *  API call to server to add attendance
         */

        //queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //passing info
        HashMap<String, String> jsonParams = new HashMap<>();

        jsonParams.put("username", user_value);
        jsonParams.put("date", currentDate);
        jsonParams.put("time", currentTime);

        //POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(ScanActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Some Error: " + error.getMessage());

                Toast.makeText(ScanActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;


            }
        };

        //adding the request to the queue
        // RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void didExitBeacon(Beacon beacon) {
        Log.d("app", "Exited beacon with UUID " + beacon.getUUID());
    }

    @Override
    public void monitoringDidFail(Error error) {
        Log.e("app", "Could not scan due to " + error.name());
    }
}
