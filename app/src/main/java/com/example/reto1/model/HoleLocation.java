package com.example.reto1.model;

public class HoleLocation {

    private boolean isValidated;
    private double lat;
    private double lng;
    private String user;
    private String id;


    public HoleLocation(){}

    public HoleLocation(double lat, double lng, boolean isValidated, String user, String id) {
        this.isValidated = isValidated;
        this.lat = lat;
        this.lng = lng;
        this.user = user;
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(boolean isValidate) {
        this.isValidated = isValidate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
