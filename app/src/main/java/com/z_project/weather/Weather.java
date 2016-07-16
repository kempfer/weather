package com.z_project.weather;

public class Weather {

    private String mPlaceId;

    private Integer mSunrise;

    private Integer mSunset;

    private Double mTemperature;

    private Double mTemperatureMin;

    private Double mTemperatureMax;

    private Double mPressure;

    private Double mHumidity;

    private String mDescription;

    private Wind mWind;

    private String icon;

    private int time;


    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public int getSunrise() {
        return mSunrise;
    }

    public void setSunrise(int mSunrise) {
        this.mSunrise = mSunrise;
    }

    public int getSunset() {
        return mSunset;
    }

    public void setSunset(int mSunset) {
        this.mSunset = mSunset;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }

    public Double getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        mTemperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public Double getPressure() {
        return mPressure;
    }

    public void setPressure(Double pressure) {
        mPressure = pressure;
    }

    public Double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Double humidity) {
        mHumidity = humidity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public void setWind(double speed, double deg) {
        mWind = new Wind(speed, deg);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public class Wind {

        private double mSpeed;

        private double mDeg;

        public Wind() {
        }

        public Wind(double speed, double deg) {
            mSpeed = speed;
            mDeg = deg;
        }

        public double getSpeed() {
            return mSpeed;
        }

        public void setSpeed(double speed) {
            mSpeed = speed;
        }

        public double getDeg() {
            return mDeg;
        }

        public void setDeg(double deg) {
            mDeg = deg;
        }
    }


}
