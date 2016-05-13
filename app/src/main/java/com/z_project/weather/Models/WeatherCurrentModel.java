package com.z_project.weather.Models;

import  com.z_project.weather.network.openweathermap.Weather;
public class WeatherCurrentModel {

    private double kel = 272.15;

    private double temp;

    private double pressure;

    private double humidity;

    private double wind;

    private String description;

    private String iconCode;

    public WeatherCurrentModel(double temp, double pressure, double humidity, double wind, String description, String iconCode) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.description = description;
        this.iconCode = iconCode;
    }

    public WeatherCurrentModel(Weather weather) {
        this.temp = Double.valueOf(weather.getMain().getTemp());
        this.pressure = Double.valueOf(weather.getMain().getPressure());
        this.humidity = Double.valueOf(weather.getMain().getHumidity());
        this.wind = weather.getWind().getSpeed();
        this.description = weather.getWeatherData().getDescription();
        this.iconCode = weather.getWeatherData().getIcon();
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
}
