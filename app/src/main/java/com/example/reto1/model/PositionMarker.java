package com.example.reto1.model;

public class PositionMarker {
    private UserLocation location;

    public PositionMarker(){}

    public PositionMarker(UserLocation location) {
        this.location = location;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }
}
