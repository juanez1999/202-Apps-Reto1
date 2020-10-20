package com.example.reto1;

import androidx.appcompat.app.AppCompatActivity;

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
    private EditText signup_userPassET;
    private EditText signup_repeatUserPassET;
    private Button signup_signinBTN;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    boolean isActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup_userEmailET = findViewById(R.id.signup_userEmailET);
        signup_userPassET = findViewById(R.id.signup_userPassET);
        signup_repeatUserPassET = findViewById(R.id.signup_repeatUserPassET);
        signup_signinBTN = findViewById(R.id.signup_signupBTN);
        signup_signinBTN.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_signupBTN:
                //Log.e(">>>", "holis" );
                writeNewUser();
                break;
        }
    }

    public void writeNewUser() {
        final String password = signup_userPassET.getText().toString();
        final String repeatPassword = signup_repeatUserPassET.getText().toString();
        final String email = signup_userEmailET.getText().toString();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    /*Log.e(">>>", data.getKey());
                    Log.e(">>>>>", email);*/
                    if (email.equals(data.getKey())) {
                        isActive = false;
                        Toast.makeText(getApplicationContext(),"Correo ya registrado",Toast.LENGTH_SHORT).show();
                    }
                }
                if (password.equals(repeatPassword) && isActive == true ){
                    myRef.child(email).child("password").setValue(password);
                    Toast.makeText(getApplicationContext(),"Correo registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),Home.class);
                    i.putExtra("name",email);
                    i.putExtra("password",password);
                    startActivity(i);
                }
                isActive = true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}