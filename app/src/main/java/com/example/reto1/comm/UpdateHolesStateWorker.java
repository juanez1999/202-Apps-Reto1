package com.example.reto1.comm;

import com.example.reto1.Home;
import com.example.reto1.model.HoleLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateHolesStateWorker extends Thread{

    private Home ref;
    private boolean isAlive;

    public UpdateHolesStateWorker(Home ref) {
        this.ref = ref;
        this.isAlive = true;
    }

    @Override
    public void run(){
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(1000);
            https.PUTrequest("https://apps-reto1.firebaseio.com/holes/"+ref.getId()+"/isValidated"+".json",gson.toJson(ref.getVerify()));
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
        this.isAlive = false;
    }

}
