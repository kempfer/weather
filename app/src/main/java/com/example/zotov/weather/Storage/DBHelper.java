package com.example.zotov.weather.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zotov on 03.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "city";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_COUNTRY = "country";

    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String COLUMN_LATITUDE = "latitude";


    final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + COLUMN_NAME + " TEXT,\n" +
            "    " + COLUMN_COUNTRY + " TEXT,\n" +
            "    " + COLUMN_LONGITUDE + " REAL,\n" +
            "    " + COLUMN_LATITUDE + " REAL\n" +
            ");";

    private static final String DB_NAME = "weather.db";

    private static final int DATABASE_VERSION = 1;


    Context mContext;

    public DBHelper(Context context, int dbVer){
        super(context, DB_NAME, null, dbVer);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
