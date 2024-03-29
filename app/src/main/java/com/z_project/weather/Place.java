package com.z_project.weather;

import java.util.UUID;

public class Place {

    private UUID mId;

    private String mName;

    private String mCountry;

    private String mRegion;

    private double mLongitude;

    private double mLatitude;

    private String mExternalId;

    public Place(UUID id) {
        mId = id;
    }

    public Place() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public String getExternalId() {
        return mExternalId;
    }

    public void setExternalId(String externalId) {
        mExternalId = externalId;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public String getDescription () {
        String description = getName();
        if(getRegion() != null && !getRegion().isEmpty()) {
            description += ", " + getRegion();
        }
        description += ", " + getCountry();

        return description;

    }

    public String getSmallDescription () {
        String description = getName();
        if(getRegion() != null && !getCountry().isEmpty()) {
            description += ", " + getCountry();
        }

        return description;
    }
}
