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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signup_userEmailET;
    private Button signup_signinBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup_userEmailET = findViewById(R.id.signup_userEmailET);
        signup_signinBTN = findViewById(R.id.signup_signupBTN);
        signup_signinBTN.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_signupBTN:
                writeNewUser();
                break;
        }
    }

    public void writeNewUser() {
        final String email = signup_userEmailET.getText().toString();

        Intent i = new Intent(getApplicationContext(), Home.class);
        i.putExtra("name", email);
        startActivity(i);

    }
}