package com.example.zotov.weather.http.openweathermap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zotov on 28.04.2016.
 */
public class Weather {



    @SerializedName("dt")
    private String time;

    @SerializedName("main")
    private Main main;

    public Weather() {
    }



    public String getTime() {
        return time;
    }

    public Main getMain() {
        return main;
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

        public double getTempC() {
            return Double.parseDouble(temp) - 272.15;
        }
    }
}

