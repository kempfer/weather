package com.z_project.weather.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.z_project.weather.Weather;
import com.z_project.weather.database.WeatherDbSchema.WeatherTable;


public class WeatherCursorWrapper extends CursorWrapper {


    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Weather getWeather() {
        Weather weather = new Weather();
        weather.setPlaceId(getString(getColumnIndex(WeatherTable.Cols.PLACE_UUID)));
        weather.setTime(getInt(getColumnIndex(WeatherTable.Cols.TIME)));
        weather.setTemperature(getDouble(getColumnIndex(WeatherTable.Cols.TEMPERATURE)));
        weather.setTemperatureMax(getDouble(getColumnIndex(WeatherTable.Cols.TEMPERATURE_MAX)));
        weather.setTemperatureMin(getDouble(getColumnIndex(WeatherTable.Cols.TEMPERATURE_MIN)));
        weather.setDescription(getString(getColumnIndex(WeatherTable.Cols.DESCRIPTION)));
        weather.setPressure(getDouble(getColumnIndex(WeatherTable.Cols.PRESSURE)));
        weather.setHumidity(getDouble(getColumnIndex(WeatherTable.Cols.HUMIDITY)));
        weather.setIcon(getString(getColumnIndex(WeatherTable.Cols.ICON)));
        weather.setWind(getDouble(getColumnIndex(WeatherTable.Cols.WIND_SPEED)),
                getDouble(getColumnIndex(WeatherTable.Cols.WIND_DEG)));
        weather.setSunset(getInt(getColumnIndex(WeatherTable.Cols.SUNSET)));
        weather.setSunrise(getInt(getColumnIndex(WeatherTable.Cols.SUNRISE)));



        return  weather;
    }
}
