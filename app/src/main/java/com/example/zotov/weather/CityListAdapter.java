package com.example.zotov.weather;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.example.zotov.weather.Storage.DBHelper;

/**
 * Created by zotov on 11.05.2016.
 */
public class CityListAdapter extends SimpleCursorAdapter {

    private DBHelper dbHelper;


    public CityListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        final String itemId = cursor.getString(cursor.getColumnIndex("_id"));

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
        dbHelper.deletePlaceById(cityId);
        Cursor newCursor = dbHelper.fetchAllPlaces();
        changeCursor(newCursor);
        notifyDataSetChanged();
    }
}
