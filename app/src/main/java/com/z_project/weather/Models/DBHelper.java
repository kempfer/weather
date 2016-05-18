package com.z_project.weather.Models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    private static volatile DBHelper instance;

    public static final String TABLE_NAME_PLACES = "places";

    public static final String TABLE_NAME_WEATHER_PLACES = "place_weather";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_COUNTRY = "country";

    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String COLUMN_LATITUDE = "latitude";

    public static final String EXTERNAL_ID = "external_id";

    public static final String COLUMN_CURRENT = "current";

    public static final String WEATHER_PLACES_COLUMN_PLACE_ID = "place_id";

    public static final String WEATHER_PLACES_COLUMN_TEMPERATURE = "temperature";
    public static final String WEATHER_PLACES_COLUMN_PRESSURE = "pressure";
    public static final String WEATHER_PLACES_COLUMN_HUMIDITY = "humidity";
    public static final String WEATHER_PLACES_COLUMN_WIND = "wind";
    public static final String WEATHER_PLACES_COLUMN_DESCRIPTION = "description";
    public static final String WEATHER_PLACES_COLUMN_ICONE_CODE = "icon_code";
    public static final String WEATHER_PLACES_COLUMN_CREATED_AT = "created_at";



    final private String DATABASE_CREATE_TABLE_PLACES = "CREATE TABLE " + TABLE_NAME_PLACES + " (\n" +
            "    id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + COLUMN_NAME + " TEXT,\n" +
            "    " + COLUMN_COUNTRY + " TEXT,\n" +
            "    " + COLUMN_LONGITUDE + " REAL,\n" +
            "    " + COLUMN_LATITUDE + " REAL,\n" +
            "    " + EXTERNAL_ID + " TEXT,\n" +
            "    " + COLUMN_CURRENT + " INTEGER \n" +
            ");";

    final private String DATABASE_CREATE_TABLE_WEATHER_PLACES = "CREATE TABLE " + TABLE_NAME_WEATHER_PLACES + " (\n" +
            "    id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + WEATHER_PLACES_COLUMN_PLACE_ID + " INTEGER,\n" +
            "    " + WEATHER_PLACES_COLUMN_TEMPERATURE + " REAL,\n" +
            "    " + WEATHER_PLACES_COLUMN_PRESSURE + " REAL,\n" +
            "    " + WEATHER_PLACES_COLUMN_HUMIDITY + " REAL,\n" +
            "    " + WEATHER_PLACES_COLUMN_WIND + " REAL,\n" +
            "    " + WEATHER_PLACES_COLUMN_DESCRIPTION + " TEXT, \n" +
            "    " + WEATHER_PLACES_COLUMN_ICONE_CODE + " TEXT, \n" +
            "    " + WEATHER_PLACES_COLUMN_CREATED_AT + " INTEGER \n" +
            ");";

    private static final String DB_NAME = "weather.db";

    private static final int DATABASE_VERSION = 1;

    Context mContext;

    public static DBHelper getInstance (Context context, int dbVer) {
        if(instance == null) {
            synchronized (DBHelper.class) {
                if(instance == null) {
                    instance = new DBHelper(context, dbVer);
                }
            }
        }

        return instance;
    }

    private DBHelper(Context context, int dbVer){
        super(context, DB_NAME, null, dbVer);
        mContext = context;
    }

    public DBHelper open() throws SQLException {
        DBHelper mDbHelper = new DBHelper(mContext, 1);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_PLACES);
        db.execSQL(DATABASE_CREATE_TABLE_WEATHER_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLACES);
        onCreate(db);
    }
}
