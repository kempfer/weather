package com.example.zotov.weather.Storage.models;

public class City {

    private Long id;

     private String name;

    private String country;

    private  double longitude;

    private  double latitude;

    public City( String name, String country, double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public Long getId() {
        return id ;
    }

    @Override
    public String toString() {
        return "City{" +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
