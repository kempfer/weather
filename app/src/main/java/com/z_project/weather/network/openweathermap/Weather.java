package com.z_project.weather.network.openweathermap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zotov on 12.05.2016.
 */
public class Weather {



    @SerializedName("dt")
    private String time;

    @SerializedName("main")
    private Main main;

    @SerializedName("name")
    private String city;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("weather")
    private List<WeatherData> weatherData;

    public Weather() {
    }



    public String getTime() {
        return time;
    }

    public Main getMain() {
        return main;
    }

    public String getCity() {
        return city;
    }

    public Wind getWind() {
        return wind;
    }

    public WeatherData getWeatherData () {
        return   weatherData.get(0);
    }

    public class Main {

        private double kel = 272.15;

        @SerializedName("temp")
        private String temp;

        @SerializedName("pressure")
        private String pressure;

        @SerializedName("humidity")
        private String humidity;

        public String getTemp() {
            return temp;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public Main(String temp, String pressure, String humidity) {
            this.temp = temp;
            this.pressure = pressure;
            this.humidity = humidity;
        }

        public int getTempCelsius() {
            Double tmp = Double.parseDouble(temp) - 272.15;
            return tmp.intValue();
        }
    }

    public class  Wind {

        @SerializedName("speed")
        private double speed;

        @SerializedName("deg")
        private double deg;

        public Wind(double speed, double deg) {
            this.speed = speed;
            this.deg = deg;
        }

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
    }

    public class WeatherData {
        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        public WeatherData(String description, String icon) {
            this.description = description;
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

}
