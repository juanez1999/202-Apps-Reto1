package com.example.reto1.comm;

import android.util.Log;

import com.example.reto1.Home;
import com.example.reto1.model.HoleLocation;
import com.example.reto1.model.HolesContainer;
import com.example.reto1.model.PositionMarker;
import com.example.reto1.model.UserLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackHolesWorker extends Thread {
    private Home ref;
    private boolean isAlive;

    public TrackHolesWorker(){}

    public TrackHolesWorker(Home ref) {
        this.ref = ref;
        this.isAlive = true;
    }

    @Override
    public void run() {
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        Gson gson = new Gson();
        while (isAlive){
            delay(5000);
            String json = https.GETrequest("https://apps-reto1.firebaseio.com/holes.json");
            Log.e(">>>", json);
            Type type = new TypeToken<HashMap<String, HolesContainer>>(){}.getType();
            HashMap<String, HolesContainer> holes = gson.fromJson(json,type);

            ArrayList<HoleLocation> locations = new ArrayList<>();
            Log.e("Locations>>>>>", String.valueOf(holes));
            holes.forEach((key,value)->{
                HolesContainer holesContainer = value;
                if(holesContainer.getLocation() != null){
                    double lat = holesContainer.getLocation().getLat();
                    double lng = holesContainer.getLocation().getLng();
                    boolean isValidated = holesContainer.getLocation().getIsValidated();
                    locations.add(new HoleLocation(lat,lng,isValidated));
                    Log.e("Locations>>>>>", String.valueOf(locations.size()));
                }
            });
            ref.makeHole(locations);
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
