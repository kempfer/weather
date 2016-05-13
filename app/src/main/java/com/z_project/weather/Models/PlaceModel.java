package com.z_project.weather.Models;


public class PlaceModel {

    private double latitude;

    private double longitude;

    private String name;

    private String country;

    private String externalId;

    private long id;

    public PlaceModel(long id, double latitude, double longitude, String name, String country, String externalId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.name = name;
        this.externalId = externalId;
        this.id = id;
    }

    public boolean isCurrentLocation () {
        return null == externalId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getExternalId() {
        return externalId;
    }

    public long getId() {
        return id;
    }
}
