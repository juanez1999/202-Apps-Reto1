package com.example.reto1.comm;

import android.util.Log;

import com.example.reto1.Home;
import com.example.reto1.model.PositionMarker;
import com.example.reto1.model.UserLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackUsersWorker extends Thread {
    private Home ref;
    private boolean isAlive;

    public TrackUsersWorker(Home ref){
        this.ref = ref;
        this.isAlive= true;
    }

    @Override
    public void run(){
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(3000);
            String json = https.GETrequest("https://apps-reto1.firebaseio.com/users.json");
            Type type = new TypeToken<HashMap<String, PositionMarker>>(){}.getType();
            HashMap<String, PositionMarker> users = gson.fromJson(json,type);

            ArrayList<UserLocation> locations = new ArrayList<>();
            users.forEach((key,value)->{
                Log.e(">>>", key);
                PositionMarker positionMarker = value;
                Log.e(">>>", String.valueOf(value));
                double lat = positionMarker.getLocation().getLat();
                double lng = positionMarker.getLocation().getLng();
                locations.add(new UserLocation(lat,lng));
            });

            ref.updateMarkers(locations);
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
