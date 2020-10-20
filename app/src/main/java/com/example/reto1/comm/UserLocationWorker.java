package com.example.reto1.comm;

import android.util.Log;

import com.example.reto1.Home;
import com.google.gson.Gson;

public class UserLocationWorker extends Thread{

    private Home ref;
    private boolean isAlive;


    public UserLocationWorker(Home ref) {
        this.ref = ref;
        isAlive = true;
    }

    @Override
    public void run(){
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(2000);
            if(ref.getUserLocation() != null) {
                https.PUTrequest("https://apps-reto1.firebaseio.com/users/"+ref.getName()+"/location.json",gson.toJson(ref.getUserLocation()));
            }
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
