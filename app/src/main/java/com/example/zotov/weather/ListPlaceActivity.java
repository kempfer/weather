package com.example.zotov.weather;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.zotov.weather.Storage.DBHelper;


public class ListPlaceActivity extends AppCompatActivity {

    ListView placeList;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_place);
        ListView placeList = (ListView) findViewById(R.id.place_list_view);

        dbHelper = new DBHelper(this, 1);

        placeList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onClickItem(id);
            }
        });


        placeList.setAdapter(getGuideAdapter());

    }

    private SimpleCursorAdapter getGuideAdapter () {
        String[] columns = new String[]{
                DBHelper.COLUMN_NAME
        };

        int[] to = new int[]{
                R.id.place_list_place_name
        };


        Cursor cursor = dbHelper.fetchAllPlaces();

        ListView placeList = (ListView) findViewById(R.id.place_list_view);

        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.place_list,
                cursor,
                columns,
                to,
                0);

        return dataAdapter;
    }



    private void onClickItem(long id) {
        //Cursor cursor = (Cursor) placeList.getItemAtPosition(position);
       // long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        System.out.println(id);
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

}
