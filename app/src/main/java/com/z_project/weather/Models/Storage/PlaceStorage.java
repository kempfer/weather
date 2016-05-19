package com.z_project.weather.Models.Storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Long2;

import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.PlaceModel;

/**
 * Created by zotov on 13.05.2016.
 */
public class PlaceStorage {

    private DBHelper dbHelper;

    private  SQLiteDatabase db;

    private WeatherStorage weatherStorage;

    public PlaceStorage(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        weatherStorage = new WeatherStorage(dbHelper);
    }

    public Cursor fetchAllPlaces() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT id as _id, * FROM " + DBHelper.TABLE_NAME_PLACES , null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        db.close();

        return mCursor;
    }

    public PlaceModel findById(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        PlaceModel place = null;
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_PLACES + " WHERE id = ?", new String[]{String.valueOf(id)});

        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        if(mCursor.getCount() > 0) {
            place = loadDataToPlaceModel(mCursor);
        }

        mCursor.close();
        db.close();
        return place;

    }

    public long addPlace(String name, String country, double latitude, double longitude, String externalId ) {
        PlaceModel placeModel = findByExternalId(externalId);
        if( placeModel == null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            initialValues.put(DBHelper.COLUMN_NAME, name);
            initialValues.put(DBHelper.COLUMN_COUNTRY, country);
            initialValues.put(DBHelper.COLUMN_LATITUDE, latitude);
            initialValues.put(DBHelper.COLUMN_LONGITUDE, longitude);
            initialValues.put(DBHelper.EXTERNAL_ID, externalId);
            initialValues.put(DBHelper.COLUMN_CURRENT, 0);

            long id =  db.insert(DBHelper.TABLE_NAME_PLACES, null, initialValues);
            db.close();
            return id;
        } else {
            return  placeModel.getId();
        }
    }

    public int deletePlaceById (String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DBHelper.TABLE_NAME_PLACES, "id = ?", new String[]{id});
        if(result > 0) {
            weatherStorage.deleteByPlaceId(id);
        }
        db.close();

        return result;
    }

    public PlaceModel findByExternalId(String externalId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_PLACES + " WHERE external_id = ?", new String[]{externalId});

        PlaceModel place = null;

        if(mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            place = loadDataToPlaceModel(mCursor);

            mCursor.close();
        }
        db.close();

        return place;

    }

    private PlaceModel loadDataToPlaceModel (Cursor cursor) {
        return new PlaceModel(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.EXTERNAL_ID))
        );
    }

    public void removeSelectedAll()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_CURRENT, 0);
        db.update(DBHelper.TABLE_NAME_PLACES, cv, "", new String[]{});
        db.close();
    }

    public int selectedById(long id) {
        return selectedById(String.valueOf(id));
    }

    public int selectedById(String id) {
        removeSelectedAll();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_CURRENT, 1);
        int result = db.update(DBHelper.TABLE_NAME_PLACES, cv, "id = ?", new String[]{id});
        db.close();

        return result;
    }

    public PlaceModel findCurrent(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME_PLACES + " WHERE " + DBHelper.COLUMN_CURRENT + " = ? LIMIT 1", new String[]{"1"});

        PlaceModel place = null;

        if(mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            place = loadDataToPlaceModel(mCursor);

            mCursor.close();
        }
        db.close();

        return place;
    }

    public int getCount() {
        return fetchAllPlaces().getCount();
    }


}
