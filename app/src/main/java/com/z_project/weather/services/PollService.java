package com.z_project.weather.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import com.z_project.weather.Place;
import com.z_project.weather.PlaceLab;
import com.z_project.weather.Weather;
import com.z_project.weather.WeatherLab;
import com.z_project.weather.http.HttpOpenWeatherMap;

import java.util.List;


public class PollService extends IntentService {

    private static final String TAG = "PollService";

    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    private PlaceLab mPlaceLab;

    private WeatherLab mWeatherLab;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static  void setServiceAlarm (Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
    }


    public PollService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "PollService onHandleIntent");
        if(!isNetworkAvailableAndConnected()) {
            return;
        }


        mPlaceLab = PlaceLab.getInstance(getApplicationContext());

        mWeatherLab = WeatherLab.getInstance(getApplicationContext());

        List<Place> places = mPlaceLab.getPlaces();

        for(Place place : places) {
            refreshWeatherToDay(place);
        }

    }

    private void refreshWeatherToDay (Place place) {
        Weather weather = new HttpOpenWeatherMap().getToDay(place);
        mWeatherLab.updateToDay(weather);

        Log.i(TAG, "Refresh Weather To Day for place " + place.getId());
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

}
