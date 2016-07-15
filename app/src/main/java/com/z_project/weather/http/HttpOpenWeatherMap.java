package com.z_project.weather.http;


import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.z_project.weather.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class HttpOpenWeatherMap extends Http {

    private static final String TAG = "HttpOpenWeatherMap";

    private static final String API_KEY = "b644c4a94e897f47d46d2666d9c42a47";

    private static final Uri ENDPOINT = Uri.parse("http://api.openweathermap.org/data/2.5/")
            .buildUpon()
            .appendQueryParameter("appid", API_KEY)
            .build();

    public Weather getToDay (double latitude, double longitude) {
        String urlString = ENDPOINT.buildUpon()
                .appendPath("weather")
                .appendQueryParameter("lat", Double.toString(latitude))
                .appendQueryParameter("lon", Double.toString(longitude))
                .toString();
        try {
            String result = getUrlString(urlString);
            return parseToDayResponse(result);
        } catch (IOException e){
            Log.e(TAG, "ERROR weather To Day" + e);
        }

        return  null;
    }

    public void  getToDay (Location location) {
        getToDay(location.getLatitude(), location.getLongitude());
    }

    public void getForecast (double latitude, double longitude) {

        String urlString = ENDPOINT.buildUpon()
                .appendPath("weather")
                .appendQueryParameter("lat", Double.toString(latitude))
                .appendQueryParameter("lon", Double.toString(longitude))
                .toString();
        try {
            String result = getUrlString(urlString);
            return parseToDayResponse(result);
        } catch (IOException e){
            Log.e(TAG, "ERROR weather To Day" + e);
        }

        return  null;
    }

    public List<Weather> getForecast (Location location) {
        return getForecast(location.getLatitude(), location.getLongitude());
    }




    private Weather parseToDayResponse (String jsonBody) {

        try {

            JSONObject jsonObject = new JSONObject(jsonBody);
            return parseWeather(jsonObject);

        } catch (JSONException e) {
            Log.e(TAG, "Failed to fetch weather to day", e);
        }

        return  null;
    }

    private Weather parseWeather (JSONObject jsonObject) throws JSONException {
        Weather weather = new Weather();
        JSONObject main = jsonObject.getJSONObject("main");
        weather.setTemperature(main.getDouble("temp"));
        weather.setTemperatureMax(main.getDouble("temp_max"));
        weather.setTemperatureMin(main.getDouble("temp_min"));
        weather.setPressure(main.getDouble("pressure"));
        weather.setHumidity(main.getDouble("humidity"));
        JSONObject wind = jsonObject.getJSONObject("wind");
        weather.setWind(wind.getDouble("speed"), wind.getDouble("deg"));

        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        JSONObject weatherMain = jsonArray.getJSONObject(0);
        weather.setDescription(weatherMain.getString("description"));
        weather.setIcon(weatherMain.getString("icon"));
        weather.setTime(jsonObject.getInt("dt"));


        return weather;
    }





}
