package com.example.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText mTextUsername;
    //EditText mTextPassword;
    TextInputEditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    Button mButtonLogin;
    TextView mTextViewLogin;
    TextView mTextViewRegister;

    /**
     * For Home network
     */
    private String url = "http://192.168.0.10:3000/login_auth";
    /**
     * For College Guest Wifi network
     */
    //private String url = "http://172.19.1.233:3000/login_auth";

    String userName;
    String passw;
    private String TAG = MainActivity.class.getName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean saveLogin;
    CheckBox cb_save;



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


        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mTextPassword= findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        mTextViewRegister = findViewById(R.id.textview_register);


        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        cb_save = findViewById(R.id.checkBox);
        editor = sharedPreferences.edit();
        editor.apply();


       /* mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });*/

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //call the function to check if the internet on
                isInternetOn();

                userName = mTextUsername.getText().toString();
                passw = mTextPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                /*if (userName.matches(emailPattern)) {
                    Toast.makeText(MainActivity.this, "Valid E-mail Address", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid E-mail Address", Toast.LENGTH_SHORT).show();
                }*/

                if (mTextUsername.getText().length() == 0 || mTextPassword.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "E-mail Address or Password is Empty", Toast.LENGTH_SHORT).show();
                }

                /**
                 API call to server to check if user exists
                 */
                validateUser();

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

        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
        if (saveLogin == true) {
            mTextUsername.setText(sharedPreferences.getString("username", null));
            mTextPassword.setText(sharedPreferences.getString("password", null));
        }
    }


    //validating the user login using API POST
    public void validateUser() {
        RequestQueue mRequest = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("userName", userName);
        jsonParams.put("passw", passw);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        /**
                         * Shared preferece login
                         * */
                        login();

                        Intent HomePage = new Intent(MainActivity.this, ScanActivity.class);
                        HomePage.putExtra("User", userName);
                        startActivity(HomePage);
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                Toast.makeText(MainActivity.this, "In-valid Email or Password", Toast.LENGTH_SHORT).show();

                // Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My user agent");
                return headers;
            }
        };
        //add the request to the queue
        mRequest.add(jsonObjectRequest);
    }

    public void login() {
        String username = mTextUsername.getText().toString();
        String password = mTextPassword.getText().toString();

        if (username.equals(userName) && password.equals(passw)) {
            //Toast.makeText(MainActivity.this, "Valid user", Toast.LENGTH_SHORT).show();

            if (cb_save.isChecked()) {
                editor.putBoolean("saveLogin", true);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();
            }
        } else {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void isInternetOn() {

        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
            }
            // Internet Available
        }else {
            //No internet
            Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
            /**
             * No internet
             */
            AlertDialog.Builder Alert = new AlertDialog.Builder(MainActivity.this);
            Alert.setMessage("Internet")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Network")
                    .setMessage("No internet connection! ")
                    .create();
            Alert.show();

        }


    }
}

