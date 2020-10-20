package com.example.reto1.model;

public class HolesContainer {
    private HoleLocation location;

    public HolesContainer(HoleLocation location) {
        this.location = location;
    }

    public HoleLocation getLocation() {
        return location;
    }

    public void setLocation(HoleLocation location) {
        this.location = location;
    }
}
