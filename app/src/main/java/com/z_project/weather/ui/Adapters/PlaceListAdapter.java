package com.z_project.weather.ui.Adapters;

import android.content.Context;

import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import com.z_project.weather.Models.DBHelper;
import com.z_project.weather.Models.PlaceModel;
import com.z_project.weather.Models.Storage.PlaceStorage;
import com.z_project.weather.R;

/**
 * Created by zotov on 13.05.2016.
 */
public class PlaceListAdapter  extends SimpleCursorAdapter {

    private PlaceStorage placeStorage;


    public PlaceListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public PlaceStorage getDbHelper() {
        return placeStorage;
    }

    public void setDbHelper(PlaceStorage placeStorage) {
        this.placeStorage = placeStorage;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        final String itemId = cursor.getString(cursor.getColumnIndex("_id"));
        final int selected = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CURRENT));
        if(selected == 1) {
            TextView textView = (TextView) view.findViewById(R.id.place_list_place_name);
            textView.setTextColor(Color.YELLOW);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.place_list_place_name);
            textView.setTextColor(Color.WHITE);
        }

        final Button deleteBtn = (Button) view.findViewById(R.id.list_item_delete_btn);

        final Cursor currentCursor = cursor;

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCity( v, itemId, currentCursor);
            }
        });
    }


    private void deleteCity (View v, String cityId, Cursor cursor) {
        placeStorage.deletePlaceById(cityId);
        refresh();
    }

    public void refresh()
    {
        Cursor newCursor = placeStorage.fetchAllPlaces();
        changeCursor(newCursor);
        notifyDataSetChanged();
    }



}