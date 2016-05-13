package com.z_project.weather.network.openweathermap;


import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApi {

    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5/weather/";

    private static final String API_KEY = "b644c4a94e897f47d46d2666d9c42a47";



    public void getToDay (Location location, Callback callback) {

        Retrofit retrofit = prepareRequest();

        Api api = retrofit.create(Api.class);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lat", Double.toString(location.getLatitude()));
        parameters.put("lon", Double.toString(location.getLongitude()));
        parameters.put("appid", API_KEY);
        Call<Weather> call = api.getToDay(parameters);

        call.enqueue(callback);

    }

    public void getToDay (double latitude, double longitude, Callback callback) {
        Location location = new Location("Test");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        getToDay(location, callback);
    }



    private Retrofit prepareRequest () {
        return  new Retrofit.Builder()
                .baseUrl(WEB_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
