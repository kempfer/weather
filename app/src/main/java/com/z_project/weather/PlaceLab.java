package com.z_project.weather;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.z_project.weather.database.WeatherBaseHelper;
import com.z_project.weather.database.PlaceCursorWrapper;
import com.z_project.weather.database.WeatherDbSchema.PlaceTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceLab {

    private static PlaceLab mInstance;

    private Context mContext;

    private SQLiteDatabase mDataBase;

    public static PlaceLab getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new PlaceLab(context);
        }

        return mInstance;
    }

    private PlaceLab(Context context) {
        mContext = context;
        mDataBase = new WeatherBaseHelper(mContext).getWritableDatabase();
    }


    public List<Place> getPlaces() {
        //fakeGenerate();
        List<Place> places = new ArrayList<>();

        PlaceCursorWrapper cursor = queryPlaces(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                places.add(cursor.getPlace());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }


        return places;
    }

    private void fakeGenerate () {
        for(int i = 0; i < 100; i++) {
            Place place = new Place();
            place.setName("Запорожье " + i);
            place.setCountry("Украина");
            place.setExternalId("set_" + i);
            place.setLongitude(10);
            place.setLatitude(11);
            add(place);

        }
    }

    public Place getPlace(UUID id) {
        PlaceCursorWrapper cursor = queryPlaces(
                PlaceTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();

            return cursor.getPlace();
        } finally {
            cursor.close();
        }
    }

    public void add(Place place) {
        ContentValues values = getContentValues(place);
        mDataBase.insert(PlaceTable.NAME, null, values);
    }

    public void update(Place place) {
        String uuidString = place.getId().toString();
        ContentValues values = getContentValues(place);

        mDataBase.update(PlaceTable.NAME, values,
                PlaceTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

    public void  delete (Place place) {
        String uuidString = place.getId().toString();

        mDataBase.delete(PlaceTable.NAME, PlaceTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }


    private ContentValues getContentValues(Place place) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlaceTable.Cols.UUID, place.getId().toString());
        contentValues.put(PlaceTable.Cols.NAME, place.getName());
        contentValues.put(PlaceTable.Cols.COUNTRY, place.getCountry());
        contentValues.put(PlaceTable.Cols.EXTERNAL_ID, place.getExternalId());
        contentValues.put(PlaceTable.Cols.LATITUDE, place.getLatitude());
        contentValues.put(PlaceTable.Cols.LONGITUDE, place.getLongitude());

        return contentValues;
    }


    private PlaceCursorWrapper queryPlaces(String whereClause, String[] whereArg) {

        Cursor cursor = mDataBase.query(
                PlaceTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArg,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new PlaceCursorWrapper(cursor);
    }


}
