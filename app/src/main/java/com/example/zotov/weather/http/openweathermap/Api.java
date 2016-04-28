package com.example.zotov.weather.http.openweathermap;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface Api {

    @GET("/data/2.5/weather")
    Call<Weather> getToDay(@QueryMap Map<String, String> parameters);

}
