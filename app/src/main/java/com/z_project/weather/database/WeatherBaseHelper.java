package com.z_project.weather.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.z_project.weather.database.WeatherDbSchema.PlaceTable;
import com.z_project.weather.database.WeatherDbSchema.WeatherTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherBase.db";

    private static final int VERSION = 1;

    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTablePlace(db);
        createTableWeather(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTablePlace (SQLiteDatabase db) {
        db.execSQL("create table "  + PlaceTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PlaceTable.Cols.UUID + " TEXT, " +
                PlaceTable.Cols.NAME + " TEXT, " +
                PlaceTable.Cols.REGION + " TEXT, " +
                PlaceTable.Cols.COUNTRY + " TEXT, " +
                PlaceTable.Cols.EXTERNAL_ID + " TEXT, " +
                PlaceTable.Cols.LATITUDE + " REAL, " +
                PlaceTable.Cols.LONGITUDE + " REAL " +
                ")"
        );
    }

    private void createTableWeather (SQLiteDatabase db) {
        db.execSQL("create table "  + WeatherTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WeatherTable.Cols.PLACE_UUID + " TEXT, " +
                WeatherTable.Cols.TEMPERATURE + " REAL, " +
                WeatherTable.Cols.TEMPERATURE_MAX + " REAL, " +
                WeatherTable.Cols.TEMPERATURE_MIN + " REAL, " +
                WeatherTable.Cols.HUMIDITY + " REAL, " +
                WeatherTable.Cols.PRESSURE + " REAL, " +
                WeatherTable.Cols.DESCRIPTION + " TEXT, " +
                WeatherTable.Cols.ICON + " TEXT, " +
                WeatherTable.Cols.WIND_SPEED + " REAL, " +
                WeatherTable.Cols.WIND_DEG + " REAL, " +
                WeatherTable.Cols.SUNRISE + " int, " +
                WeatherTable.Cols.SUNSET + " int, " +
                WeatherTable.Cols.UPDATED_AT + " int, " +
                WeatherTable.Cols.TIME + " int " +
            ")"
        );
    }
}
