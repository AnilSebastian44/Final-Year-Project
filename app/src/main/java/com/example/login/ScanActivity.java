package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mobi.inthepocket.android.beacons.ibeaconscanner.Beacon;
import mobi.inthepocket.android.beacons.ibeaconscanner.Error;
import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;

public class ScanActivity extends AppCompatActivity implements IBeaconScanner.Callback {

    private static final String TAG = "ScanActivity";
    TextView uuid, matching, user, date, time;
    Button view_btn, add_attendance;
    DbHelper db;
    String uuid_match = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        db = new DbHelper(this);

        // initialize
        IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
        IBeaconScanner.getInstance().setCallback(this);

        uuid = findViewById(R.id.tv_uuid);
        matching = findViewById(R.id.tv_uuid_match);
        user = findViewById(R.id.tv_user);
        date = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);


        //attancancd view button
        view_btn = findViewById(R.id.view_record);
        add_attendance = findViewById(R.id.btn_add);
        add_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_attendance = new Intent(ScanActivity.this, ManualRecord.class);
                startActivity(add_attendance);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

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

            //Blue beacon
            Beacon beacon1 = Beacon.newBuilder()
                    .setUUID("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                    .setMajor(21443)
                    .setMinor(21804)
                    .build();
            IBeaconScanner.getInstance().startMonitoring(beacon1);
        }
    }

    @Override
    public void didEnterBeacon(Beacon beacon) {
        Log.d("app", "Entered beacon with UUID " + beacon.getUUID() + beacon.getMinor() + beacon.getMajor());

        Toast.makeText(ScanActivity.this, beacon.getUUID() + "Found assigned beacons UUID", Toast.LENGTH_SHORT).show();


        if (beacon.getUUID().toString() != null && beacon.getUUID().toString().equals(uuid_match)) {

            //setting text
            uuid.setText("UUID: " + beacon.getUUID().toString());

            matching.setText("UUID IS MATCHING");

            final String value;

            //getting value of user login from MainActivity
            Bundle extras = getIntent().getExtras();
            //final String value;
            if (extras != null) {
                value = (String) extras.get("User");
                user.setText(value);


                //getting the current time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String currentTime = "Registered Time : " + dateFormat.format(calendar.getTime());
                time.setText(currentTime);

                //getting the current date
                SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyy");
                String currentDate = "Registered Date: " + timeFormat.format(calendar.getTime());
                date.setText(currentDate);


                //adding username, date and time to the database table RecordUsers

                if (value.length() > 0 && currentDate != null && currentTime != null) {

                    boolean res = db.addUserRecord(value, currentDate, currentTime);
                    if (res == true) {

                        Toast.makeText(ScanActivity.this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show();
                        view_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent View_attandance = new Intent(ScanActivity.this, ViewAttendance.class);
                                View_attandance.putExtra("User", value);
                                startActivity(View_attandance);
                            }
                        });
                    }

                }

                //record attandance
            } else {
                matching.setText("UUID NOT MATCHING");
                Toast.makeText(ScanActivity.this, "Beacon UUID not matching", Toast.LENGTH_SHORT).show();
            }

        }
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
