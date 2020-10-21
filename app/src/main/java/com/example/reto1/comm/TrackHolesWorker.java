package com.example.reto1.comm;

import android.util.Log;

import com.example.reto1.Home;
import com.example.reto1.model.HoleLocation;
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
            //Log.e(">>>", json);
            Type type = new TypeToken<HashMap<String, HoleLocation>>() {
            }.getType();
            HashMap<String, HoleLocation> holes = gson.fromJson(json, type);

            ArrayList<HoleLocation> locations = new ArrayList<>();
            if(holes != null ){
                holes.forEach((key, value) -> {
                    HoleLocation holeLocation = value;
                    //if(holeLocation != null){
                    double lat = holeLocation.getLat();
                    double lng = holeLocation.getLng();
                    boolean isValidated = holeLocation.getIsValidated();
                    String user = holeLocation.getUser();
                    String id = holeLocation.getId();
                    locations.add(new HoleLocation(lat, lng, isValidated,user,id));
                    //Log.e("Locations>>>>>", String.valueOf(locations.size()));
                    //}
                });
                ref.makeHole(locations);
                ref.verifyHole(locations);
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
        this.isAlive = false;
    }


}
