package com.example.reto1.model;

public class HoleLocation {

    private double lat;
    private double lng;
    private boolean isValidated;

    public HoleLocation(){}

    public HoleLocation(double lat, double lng, boolean isValidated) {
        this.lat = lat;
        this.lng = lng;
        this.isValidated = isValidated;
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

    public boolean isValidate() {
        return isValidated;
    }

    public void setValidate(boolean isValidate) {
        this.isValidated = isValidate;
    }
}
