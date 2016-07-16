package com.z_project.weather;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.z_project.weather.database.WeatherBaseHelper;
import com.z_project.weather.database.WeatherCursorWrapper;
import com.z_project.weather.database.WeatherDbSchema.WeatherTable;

import java.util.Calendar;

public class WeatherLab {

    private Context mContext;

    private SQLiteDatabase mDataBase;

    private static WeatherLab mInstance;

    private WeatherLab(Context context) {
        mContext = context;
        mDataBase = new WeatherBaseHelper(mContext).getWritableDatabase();
    }


    public static WeatherLab getInstance (Context context) {
        if(mInstance == null) {
            mInstance = new WeatherLab(context);
        }

        return mInstance;
    }

    public void add (Weather weather) {
        ContentValues contentValues = getContentValues(weather);
        mDataBase.insert(WeatherTable.NAME, null, contentValues);
    }

    public Weather getDoDay (Place place) {
        WeatherCursorWrapper cursorWrapper = queryWeather(
                WeatherTable.Cols.PLACE_UUID + " = ?",
                new String[]{place.getId().toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();

            return cursorWrapper.getWeather();
        } finally {
            cursorWrapper.close();
        }


    }

    private ContentValues getContentValues(Weather weather) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherTable.Cols.PLACE_UUID, weather.getPlaceId());
        contentValues.put(WeatherTable.Cols.DESCRIPTION, weather.getDescription());
        contentValues.put(WeatherTable.Cols.HUMIDITY, weather.getHumidity());
        contentValues.put(WeatherTable.Cols.ICON, weather.getIcon());
        contentValues.put(WeatherTable.Cols.PRESSURE, weather.getPressure());
        contentValues.put(WeatherTable.Cols.SUNRISE, weather.getSunrise());
        contentValues.put(WeatherTable.Cols.SUNSET, weather.getSunset());
        contentValues.put(WeatherTable.Cols.TEMPERATURE, weather.getTemperature());
        contentValues.put(WeatherTable.Cols.TEMPERATURE_MAX, weather.getTemperatureMax());
        contentValues.put(WeatherTable.Cols.TEMPERATURE_MIN, weather.getTemperatureMin());
        contentValues.put(WeatherTable.Cols.TIME, weather.getTime());
        contentValues.put(WeatherTable.Cols.UPDATED_AT, Calendar.getInstance().getTime().getTime());

        return contentValues;
    }

    private WeatherCursorWrapper queryWeather(String whereClause, String[] whereArg) {

        Cursor cursor = mDataBase.query(
                WeatherTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArg,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new WeatherCursorWrapper(cursor);
    }
}
