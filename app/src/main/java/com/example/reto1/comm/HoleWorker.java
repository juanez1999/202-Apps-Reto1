package com.example.reto1.comm;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.reto1.Home;
import com.example.reto1.model.HoleLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.UUID;

public class HoleWorker extends Thread {

    private Home ref;
    private boolean isAlive;


    public HoleWorker(Home ref) {
        this.ref = ref;
        this.isAlive = true;
    }

    @Override
    public void run(){
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(1000);
            if(ref.getCurrentLocation() != null) {
                https.PUTrequest("https://apps-reto1.firebaseio.com/holes/"+ref.getCurrentLocation().getId()+".json",gson.toJson(ref.getCurrentLocation()));
            }
            isAlive = false;
        }

    }

    public void delay(long time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void finish() {
    }
}
