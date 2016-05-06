package com.example.zotov.weather.Storage.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by zotov on 05.05.2016.
 */
@Table
public class City extends SugarRecord {

    private Long id;

     private String name;

    private String country;

    private  double longitude;

    private  double latitude;

    public City(){

    }

    public City( String name, String country, double longitude, double latitude) {
        super();
        this.name = name;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public Long getId() {
        return id;
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
