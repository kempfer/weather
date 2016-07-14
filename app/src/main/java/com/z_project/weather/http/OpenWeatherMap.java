package com.z_project.weather.http;


import android.location.Location;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

public class OpenWeatherMap extends Http {

    private static final String TAG = "OpenWeatherMap";

    private static final String API_KEY = "b644c4a94e897f47d46d2666d9c42a47";

    private static final Uri ENDPOINT = Uri.parse("http://api.openweathermap.org/data/2.5/weather/")
            .buildUpon()
            .appendQueryParameter("appid", API_KEY)
            .build();

    public void getToDay (double latitude, double longitude) {
        String urlString = ENDPOINT.buildUpon()
                .appendQueryParameter("lat", Double.toString(latitude))
                .appendQueryParameter("lon", Double.toString(longitude))
                .toString();
        try {
            String result = getUrlString(urlString);
            Log.i(TAG, result);
        } catch (IOException e){

        }
    }

    public void  getToDay (Location location) {
        getToDay(location.getLatitude(), location.getLongitude());
    }





}
