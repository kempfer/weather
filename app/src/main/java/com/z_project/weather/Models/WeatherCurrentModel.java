package com.z_project.weather.Models;

import  com.z_project.weather.network.openweathermap.Weather;

import java.sql.Timestamp;

public class WeatherCurrentModel {

    final private  double kel = 272.15;

    private double temp;

    private double pressure;

    private double humidity;

    private double wind;

    private long lastUpdate;

    private String description;

    private String iconCode;

    public WeatherCurrentModel(double temp, double pressure, double humidity, double wind, String description, String iconCode, long lastUpdate) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.description = description;
        this.iconCode = iconCode;
        this.lastUpdate = lastUpdate;

    }

    public WeatherCurrentModel(double temp, double pressure, double humidity, double wind, String description, String iconCode) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.description = description;
        this.iconCode = iconCode;
        java.util.Date date= new java.util.Date();
        this.lastUpdate = (new Timestamp(date.getTime())).getTime();
    }

    public WeatherCurrentModel(Weather weather) {
        this.temp = Double.valueOf(weather.getMain().getTemp());
        this.pressure = Double.valueOf(weather.getMain().getPressure());
        this.humidity = Double.valueOf(weather.getMain().getHumidity());
        this.wind = weather.getWind().getSpeed();
        this.description = weather.getWeatherData().getDescription();
        this.iconCode = weather.getWeatherData().getIcon();
        java.util.Date date= new java.util.Date();
        this.lastUpdate = (new Timestamp(date.getTime())).getTime();
    }

    public double getTemp() {
        return temp;
    }

    public int getTempCelsius() {
        Double tmp = temp - kel;
        return tmp.intValue();
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWind() {
        return wind;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
