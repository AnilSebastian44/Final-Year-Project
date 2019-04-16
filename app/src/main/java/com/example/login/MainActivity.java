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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home_Activity.class);
        startActivity(intent);
        finish();
    }

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

                String userName = mTextUsername.getText().toString().trim();
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
