package com.z_project.weather.Models.Storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.WeatherCurrentModel;

import java.sql.Timestamp;


public class WeatherStorage {

    private DBHelper dbHelper;



    public WeatherStorage(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public WeatherCurrentModel findByPlaceId(long id) {
        return findByPlaceId(String.valueOf(id));
    }

    public WeatherCurrentModel findByPlaceId(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        WeatherCurrentModel weatherModel = null;
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_WEATHER_PLACES + " WHERE place_id = ?", new String[]{String.valueOf(id)});

        if(mCursor != null) {
            mCursor.moveToFirst();
            if(mCursor.getCount() > 0) {
                weatherModel = loadDataToWeatherModel(mCursor);
            }

            mCursor.close();
        }

        db.close();
        return weatherModel;
    }

    public long save (String placeId, WeatherCurrentModel weatherModel) {
        deleteByPlaceId(placeId);
        java.util.Date date= new java.util.Date();
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_PLACE_ID, placeId);
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_TEMPERATURE, weatherModel.getTemp());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_HUMIDITY, weatherModel.getHumidity());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_PRESSURE, weatherModel.getPressure());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_WIND, weatherModel.getWind());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_DESCRIPTION, weatherModel.getDescription());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_ICONE_CODE, weatherModel.getIconCode());
        initialValues.put(DBHelper.WEATHER_PLACES_COLUMN_CREATED_AT, (new Timestamp(date.getTime())).getTime());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id =  db.insert(DBHelper.TABLE_NAME_WEATHER_PLACES, null, initialValues);
        db.close();

        return id;
    }

    public long save (long placeId, WeatherCurrentModel weatherModel) {
        return save(String.valueOf(placeId), weatherModel);
    }

    public int deleteByPlaceId (String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DBHelper.TABLE_NAME_WEATHER_PLACES, "place_id = ?", new String[]{id});
        db.close();

        return result;
    }

    public int deleteByPlaceId(long id) {
        return deleteByPlaceId(String.valueOf(id));
    }

    private WeatherCurrentModel loadDataToWeatherModel (Cursor cursor) {
        return new WeatherCurrentModel(
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_TEMPERATURE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_PRESSURE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_HUMIDITY)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_WIND)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_ICONE_CODE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.WEATHER_PLACES_COLUMN_TEMPERATURE))
        );
    }

}
