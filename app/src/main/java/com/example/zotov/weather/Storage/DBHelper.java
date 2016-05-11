package com.example.zotov.weather.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zotov.weather.Storage.models.City;

/**
 * Created by zotov on 03.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "city";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_COUNTRY = "country";

    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String COLUMN_LATITUDE = "latitude";

    public static final String EXTERNAL_ID = "external_id";



    final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (\n" +
            "    id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + COLUMN_NAME + " TEXT,\n" +
            "    " + COLUMN_COUNTRY + " TEXT,\n" +
            "    " + COLUMN_LONGITUDE + " REAL,\n" +
            "    " + COLUMN_LATITUDE + " REAL,\n" +
            "    " + EXTERNAL_ID + " TEXT\n" +
            ");";

    private static final String DB_NAME = "weather.db";

    private static final int DATABASE_VERSION = 1;


    Context mContext;

    public DBHelper(Context context, int dbVer){
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

    public Cursor fetchAllPlaces() {


        SQLiteDatabase db = this.getWritableDatabase();


        Cursor mCursor = db.rawQuery("SELECT id as _id, name FROM city", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        db.close();

        return mCursor;
    }

    public City findById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM city WHERE id = ?", new String[]{String.valueOf(id)});

        if(mCursor != null) {
            mCursor.moveToFirst();
        }

        City city = new City(
                mCursor.getString(mCursor.getColumnIndexOrThrow("name")),
                mCursor.getString(mCursor.getColumnIndexOrThrow("country")),
                mCursor.getDouble(mCursor.getColumnIndexOrThrow("latitude")),
                mCursor.getDouble(mCursor.getColumnIndexOrThrow("longitude")),
                mCursor.getLong(mCursor.getColumnIndexOrThrow("id"))
                );

        db.close();
        mCursor.close();

        return city;

    }

    public long addPlace(String name, String country, double latitude, double longitude, String externalId ) {
        City city = findByExternalId(externalId);
        if( city == null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(COLUMN_NAME, name);
            initialValues.put(COLUMN_COUNTRY, country);
            initialValues.put(COLUMN_LATITUDE, latitude);
            initialValues.put(COLUMN_LONGITUDE, longitude);
            initialValues.put(EXTERNAL_ID, externalId);

            long id =  db.insert("city", null, initialValues);
            db.close();
            return id;
        } else {
            return  city.getId();
        }
    }

    public int deletePlaceById (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("city", "id = ?", new String[]{id});
        db.close();

        return result;
    }

    public City findByExternalId(String externalId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM city WHERE external_id = ?", new String[]{externalId});

        City city = null;

        if(mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            city = new City(
                    mCursor.getString(mCursor.getColumnIndexOrThrow("name")),
                    mCursor.getString(mCursor.getColumnIndexOrThrow("country")),
                    mCursor.getDouble(mCursor.getColumnIndexOrThrow("latitude")),
                    mCursor.getDouble(mCursor.getColumnIndexOrThrow("longitude")),
                    mCursor.getLong(mCursor.getColumnIndexOrThrow("id"))
            );



            mCursor.close();
        }


        db.close();


        return city;

    }


}
