package com.example.reto1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.reto1.comm.HTTPSWebUtilDomi;
import com.example.reto1.comm.HoleWorker;
import com.example.reto1.comm.TrackHolesWorker;
import com.example.reto1.comm.TrackUsersWorker;
import com.example.reto1.comm.UpdateHolesStateWorker;
import com.example.reto1.comm.UserLocationWorker;
import com.example.reto1.model.HoleLocation;
import com.example.reto1.model.UserLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Home extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener, RegisterHoleFragment.OnRegisterListener {

    private GoogleMap mMap;
    private HTTPSWebUtilDomi https;
    private LocationManager manager;
    //private Marker me;
    private Button btnAdd;
    private Button btnConfirm;
    private Gson gson;
    private HoleWorker holeWorker;
    private UpdateHolesStateWorker updateHolesStateWorker;
    private UserLocationWorker locationWorker;
    private TrackUsersWorker trackUsersWorker;
    private TrackHolesWorker trackHolesWorker;

    private HoleLocation currentLocation;
    private UserLocation userLocation;

    private String name;
    private String password;
    private String id;
    private Boolean verify;

    private RegisterHoleFragment dialog;
    private String address;

    private ArrayList<Marker> usersMarkers;
    private ArrayList<Marker> holesMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnAdd = findViewById(R.id.btnAdd);
        btnConfirm = findViewById(R.id.btnConfirm);
        gson = new Gson();
        https = new HTTPSWebUtilDomi();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        btnAdd.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        usersMarkers = new ArrayList<>();
        holesMarkers = new ArrayList<>();
        verify = true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,2,this);
        setInitialPos();


        locationWorker = new UserLocationWorker(this);
        locationWorker.start();
        trackUsersWorker = new TrackUsersWorker(this);
        trackUsersWorker.start();
        trackHolesWorker = new TrackHolesWorker(this);
        trackHolesWorker.start();
        btnConfirm.setVisibility(View.INVISIBLE);
    }

    public void onDestroy(){
        holeWorker.finish();
        locationWorker.finish();
        trackUsersWorker.finish();
        trackHolesWorker.finish();
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    public void setInitialPos(){
       Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            updateMyLocation(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateMyLocation(location);
    }

    public void updateMyLocation (Location location){
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        /*if(me==null) {
            me = mMap.addMarker(new MarkerOptions().position(myPos).title("Mi posición"));
        }else{
            me.setPosition(myPos);
        }*/
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 17));
        currentLocation = new HoleLocation(location.getLatitude(), location.getLongitude(),false,name, UUID.randomUUID().toString());
        userLocation = new UserLocation(location.getLatitude(),location.getLongitude());
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    @SuppressLint("MissingPermission")
    public void onClick(View view) {
      Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        switch (view.getId()){
            case R.id.btnAdd:
                getAddressFromLocation(location.getLatitude(), location.getLongitude());
                //Mostrar el modal
                dialog = RegisterHoleFragment.newInstance(location.getLatitude()+","+location.getLongitude(), address);
                dialog.setListener(this);
                dialog.show(getSupportFragmentManager(), "ConfirmHole");
                break;
            case R.id.btnConfirm:
                updateHolesStateWorker = new UpdateHolesStateWorker(this);
                updateHolesStateWorker.start();
                Toast.makeText(this,"Holis",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRegister() {
        dialog.dismiss();
        holeWorker = new HoleWorker(this);
        holeWorker.start();
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i <= fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }
                address = strAddress.toString();
            } else {
                address = "Buscando la dirección actual";
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo obtener la dirección :(", Toast.LENGTH_SHORT);
        }
    }

    public void updateMarkers(ArrayList<UserLocation> locations){
        runOnUiThread(
                ()->{
                    for (int i = 0; i < usersMarkers.size(); i++) {
                        Marker m = usersMarkers.get(i);
                        m.remove();
                    }
                    usersMarkers.clear();

                    for (int i = 0; i < locations.size(); i++) {
                        UserLocation location = locations.get(i);
                        LatLng latLng = new LatLng(location.getLat(),location.getLng());
                        Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
                        usersMarkers.add(m);
                    }
        });
    }

    public void makeHole(ArrayList<HoleLocation> locations){
        runOnUiThread(
                ()->{
                    for (int i = 0; i < holesMarkers.size(); i++) {
                        Marker m = holesMarkers.get(i);
                        m.remove();
                    }
                    holesMarkers.clear();
                    Log.e(">>>>>>", String.valueOf(locations.size()));

                    for (int i = 0; i < locations.size(); i++) {
                        HoleLocation location = locations.get(i);
                        LatLng latLng = new LatLng(location.getLat(),location.getLng());
                        boolean isValidated = location.getIsValidated();
                        Log.e(">>>>>>", locations.get(i).toString());
                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        );
                        holesMarkers.add(m);
                    }
                }
        );
    }

    @SuppressLint("MissingPermission")
    public void verifyHole(ArrayList<HoleLocation> locations){
        Location myPos = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        for (int i = 0; i < locations.size(); i++) {
            HoleLocation location = locations.get(i);

            LatLng point1 = new LatLng(myPos.getLatitude(),myPos.getLongitude());
            LatLng point2 = new LatLng(location.getLat(),location.getLng());

            if(!name.equals(location.getUser()) && location.getIsValidated() == false){
                int distance = (int) SphericalUtil.computeDistanceBetween(point1,point2);
                id = location.getId();
                Log.e(">>>", location.getId());
                Log.e(">>>", distance+"");
                if(distance <= 5){
                    runOnUiThread(()->{
                        btnConfirm.setVisibility(View.VISIBLE);
                    });
                }else{
                    runOnUiThread(()->{
                        btnConfirm.setVisibility(View.INVISIBLE);
                    });
                }
            }
        }
    }

    public HoleLocation getCurrentLocation() {
        return currentLocation;
    }

    public Boolean getVerify() {
        return verify;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }
}