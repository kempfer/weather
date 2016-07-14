package com.z_project.weather.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.z_project.weather.database.WeatherDbSchema.PlaceTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherBase.db";

    private static final int VERSION = 1;

    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
