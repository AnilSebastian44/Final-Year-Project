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

public class LecturerLoginActivity extends AppCompatActivity {

    EditText mTextUsername;
    //EditText mTextPassword;
    TextInputEditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewLogin;
    TextView mTextViewRegister;

    /**
     * For Home network
     */
    private String url = "http://192.168.0.10:3000/lect_login_auth";
    /**
     * For College Guest Wifi network
     */
    //private String url = "http://172.19.1.233:3000/lect_login_auth";

    String userName;
    String passw;
    private String TAG = LoginActivity.class.getName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean saveLogin;
    CheckBox cb_save;



    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lect_login);

        //showProgressingView();
        //hideProgressingView();

        mTextUsername = findViewById(R.id.edittext_username);
       // mTextPassword = findViewById(R.id.edittext_password);
        mTextPassword=findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        // mTextViewRegister = findViewById(R.id.textview_register);


        sharedPreferences = getSharedPreferences("LoginLect", MODE_PRIVATE);
        cb_save = findViewById(R.id.checkBox);
        editor = sharedPreferences.edit();
        editor.apply();


       /* mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LecturerLoginActivity.this, RegisterActivity.class);
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
                    Toast.makeText(LecturerLoginActivity.this, "Valid E-mail Address", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LecturerLoginActivity.this, "Invalid E-mail Address", Toast.LENGTH_SHORT).show();
                }*/

                if (mTextUsername.getText().length() == 0 || mTextPassword.getText().length() == 0) {
                    Toast.makeText(LecturerLoginActivity.this, "E-mail Address or Password is Empty", Toast.LENGTH_SHORT).show();
                }


                /**
                 API call to server to check if user exists
                 */
                validateUser();

            }
        });

        saveLogin = sharedPreferences.getBoolean("saveLogin", true);
        if (saveLogin == true) {
            mTextUsername.setText(sharedPreferences.getString("Username", null));
            mTextPassword.setText(sharedPreferences.getString("Password", null));
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
                         * Shared preferences login
                         * */
                        login();

                        Intent HomePage = new Intent(LecturerLoginActivity.this, ManualRecord.class);
                        HomePage.putExtra("User", userName);
                        startActivity(HomePage);
                        Toast.makeText(LecturerLoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(LecturerLoginActivity.this, "In-valid Email or Password", Toast.LENGTH_LONG).show();
                //Toast.makeText(LecturerLoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    /**
     * Adding user login info to Shared preferences
     */
    public void login() {
        String username = mTextUsername.getText().toString();
        String password = mTextPassword.getText().toString();

        if (username.equals(userName) && password.equals(passw)) {
            Toast.makeText(LecturerLoginActivity.this, "Valid user", Toast.LENGTH_SHORT).show();

            if (cb_save.isChecked()) {
                editor.putBoolean("saveLogin", true);
                editor.putString("Username", username);
                editor.putString("Password", password);
                editor.apply();
            }
        } else {
            Toast.makeText(LecturerLoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(LecturerLoginActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
            /**
             * No internet
             */
            AlertDialog.Builder Alert = new AlertDialog.Builder(LecturerLoginActivity.this);
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
