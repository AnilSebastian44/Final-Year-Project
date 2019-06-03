package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAttendance extends AppCompatActivity {
    private static final String TAG = "ViewAttendance";



    DbHelper db;
    ListView listView;
    String user_value;
    /**
     *
     * For Home network
     */
    private String url = "http://192.168.0.10:3000/view_attendance";

    //For college Guest Wifi 172.19.1.233
    /**
     *
     * For College Guest Wifi network
     */
    //private String url = "http://172.19.1.233:3000/view_attendance";

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ScanActivity.class);
        this.recreate();
        startActivity(intent);
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance);




        listView = findViewById(R.id.listView);
        db = new DbHelper(this);

        ArrayList<String> list = new ArrayList<>();

        //getting value of user login from LoginActivity
       Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_value = (String) extras.get("User");
            //user.setText(value);


            //queue
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            //passing info
            HashMap<String, String> jsonParams = new HashMap<>();

            jsonParams.put("username", user_value);


            //POST
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            Toast.makeText(ViewAttendance.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Some Error: " + error.getMessage());

                    Toast.makeText(ViewAttendance.this, error.toString(), Toast.LENGTH_SHORT).show();

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


            //Cursor data = db.getRecordData(value);*/


            //Cursor data = db.getRecordData();

            Cursor data;
            //getting specific data
            //data= db.getSpecificData(value);

            //getting all the data
            data = db.getRecordData();


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
