package com.example.reto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText login_userEmailET;
    private EditText login_userPassET;
    private Button login_loginBTN;
    private Button login_signupBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_userEmailET = findViewById(R.id.login_userEmailET);
        login_userPassET = findViewById(R.id.login_userPassET);
        login_loginBTN = findViewById(R.id.login_loginBTN);
        login_signupBTN = findViewById(R.id.login_signupBTN);
        login_loginBTN.setOnClickListener(this);
        login_signupBTN.setOnClickListener(this);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_loginBTN:

                break;
            case R.id.login_signupBTN:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;
        }
    }
}