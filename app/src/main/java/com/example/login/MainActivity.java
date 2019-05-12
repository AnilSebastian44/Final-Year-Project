package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DbHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    Button mButtonLogin;
    TextView mTextViewLogin;
    TextView mTextViewRegister;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    //private String url = "http://192.168.0.129:3000/course";
    private String url = "http://192.168.0.129:3000/login_auth";

    private String TAG = MainActivity.class.getName();


    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home_Activity.class);
        startActivity(intent);
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //showProgressingView();
        //hideProgressingView();


        db = new DbHelper(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        mTextViewRegister = findViewById(R.id.textview_register);


        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*final String userName = mTextUsername.getText().toString().trim();
                final String passw = mTextPassword.getText().toString().trim();*/

                final String userName = mTextUsername.getText().toString();
                final String passw = mTextPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (userName.matches(emailPattern)) {
                    Toast.makeText(MainActivity.this, "Valid E-mail Address", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid E-mail Address", Toast.LENGTH_SHORT).show();
                }

                String pass = mTextPassword.getText().toString().trim();
                if (mTextUsername.getText().length() == 0 || mTextPassword.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "E-mail Address or Password is Empty", Toast.LENGTH_SHORT).show();
                }

                boolean res = db.checkUser(userName, pass);
                if (res == true && userName != null && pass != null) {

                    /*//if user name exists
                    if(db.rowIdExists(user)){
                        Toast.makeText(MainActivity.this, "Username exist", Toast.LENGTH_SHORT).show();
                    }*/

                    Intent HomePage = new Intent(MainActivity.this, ScanActivity.class);
                    HomePage.putExtra("User", userName);
                    startActivity(HomePage);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }


                /*
                API call to server to check if user exists
                 */


                RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());

                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put("userName", userName);
                jsonParams.put("passw", passw);


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new JSONObject(jsonParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent HomePage = new Intent(MainActivity.this, ScanActivity.class);
                                HomePage.putExtra("User", userName);
                                startActivity(HomePage);


                                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers  = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", "My useragent");
                        return headers;
                    }
                };

                //add the request tot he que
               // RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());

                mRequest.add(jsonObjectRequest);




//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//                if (userName.matches(emailPattern)) {
//                    Toast.makeText(MainActivity.this, "Valid E-mail Address", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Invalid E-mail Address", Toast.LENGTH_SHORT).show();
//                }
//
//                String pass = mTextPassword.getText().toString().trim();
//                if (mTextUsername.getText().length() == 0 || mTextPassword.getText().length() == 0) {
//                    Toast.makeText(MainActivity.this, "E-mail Address or Password is Empty", Toast.LENGTH_SHORT).show();
//                }
//
//                boolean res = db.checkUser(userName, pass);
//                if (res == true && userName != null && pass != null) {
//
//                    /*//if user name exists
//                    if(db.rowIdExists(user)){
//                        Toast.makeText(MainActivity.this, "Username exist", Toast.LENGTH_SHORT).show();
//                    }*/
//
//                    Intent HomePage = new Intent(MainActivity.this, ScanActivity.class);
//                    HomePage.putExtra("User", userName);
//                    startActivity(HomePage);
//                } else {
//                    Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }




    public void showProgressingView() {

        if (!isProgressShowing) {
            View view = findViewById(R.id.progressBar1);
            view.bringToFront();
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }
}
