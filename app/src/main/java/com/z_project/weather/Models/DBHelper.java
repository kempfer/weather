package com.z_project.weather.Models;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    private static volatile DBHelper instance;

    public static final String TABLE_NAME = "place";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_COUNTRY = "country";

    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String COLUMN_LATITUDE = "latitude";

    public static final String EXTERNAL_ID = "external_id";

    public static final String COLUMN_CURRENT = "current";



    final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (\n" +
            "    id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + COLUMN_NAME + " TEXT,\n" +
            "    " + COLUMN_COUNTRY + " TEXT,\n" +
            "    " + COLUMN_LONGITUDE + " REAL,\n" +
            "    " + COLUMN_LATITUDE + " REAL,\n" +
            "    " + EXTERNAL_ID + " TEXT,\n" +
            "    " + COLUMN_CURRENT + " INTEGER \n" +
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
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE);
        onCreate(db);
    }
}
