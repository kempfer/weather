package com.z_project.weather.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.z_project.weather.Place;
import com.z_project.weather.database.WeatherDbSchema.PlaceTable;

import java.util.UUID;


public class PlaceCursorWrapper extends CursorWrapper {

    public PlaceCursorWrapper(Cursor cursor) {
         super(cursor);
    }

    public Place getPlace() {
        String uuidString = getString(getColumnIndex(PlaceTable.Cols.UUID));
        String name = getString(getColumnIndex(PlaceTable.Cols.NAME));
        String country = getString(getColumnIndex(PlaceTable.Cols.COUNTRY));
        Double latitude = getDouble(getColumnIndex(PlaceTable.Cols.LATITUDE));
        Double longitude = getDouble(getColumnIndex(PlaceTable.Cols.LONGITUDE));
        String externalId = getString(getColumnIndex(PlaceTable.Cols.EXTERNAL_ID));

        Place place = new Place(UUID.fromString(uuidString));
        place.setName(name);
        place.setCountry(country);
        place.setExternalId(externalId);
        place.setLatitude(latitude);
        place.setLongitude(longitude);


        return place;
    }
}
