//package com.example.login;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    DbHelper db;
//    EditText mTextUsername;
//    EditText mTextPassword;
//    EditText mTextCnfPassword;
//    Button mButtonRegister;
//    TextView mTextViewLogin;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//
//        db = new DbHelper(this);
//        mTextUsername = findViewById(R.id.edittext_username);
//        mTextPassword = findViewById(R.id.edittext_password);
//        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
//        mButtonRegister = findViewById(R.id.button_register);
//        mTextViewLogin = findViewById(R.id.textview_login);
//        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(LoginIntent);
//            }
//        });
//
//
//        mButtonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userName = mTextUsername.getText().toString().trim();
//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//
//                String pass = mTextPassword.getText().toString().trim();
//                String cnf_pass = mTextCnfPassword.getText().toString().trim();
//
//                if (pass.equals(cnf_pass)) {
//
//                    //checking if the user already exist in the database
//                    boolean info = db.checkUser(userName, pass);
//                    if (info == true && userName != null && pass != null) {
//
//                        Toast.makeText(RegisterActivity.this, "User already registered", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        //adding the user to database
//                        boolean res = db.addUser(userName, pass);
//
//                        if (res == true && userName.length() > 0 && pass.length() > 0) {
//
//                            Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
//                            Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(moveToLogin);
//                        }
//
//                        if (userName.matches(emailPattern) /*&& db.getData(userName)*/) {
//                            Toast.makeText(RegisterActivity.this, "Valid E-mail Address", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "Invalid E-mail Address", Toast.LENGTH_SHORT).show();
//
//                            Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}