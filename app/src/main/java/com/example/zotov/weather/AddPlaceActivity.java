package com.example.zotov.weather;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import com.example.zotov.weather.Storage.DBHelper;

public class AddPlaceActivity extends AppCompatActivity {

    SearchView searchView;

    // Listview Data
    String places[] = {"Запорожье", "Киев", "Харьков", "Херсон", "Николаев",
            "Одесса", "Львов"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        searchView = (SearchView) findViewById(R.id.searchView);
        try {
            searchView.setQuery("test",true);
        } catch (NullPointerException e) {

        }

        searchView.setFocusable(true);
        searchView.setIconified(false);

        DBHelper dbHelper  = new DBHelper(this, 1);

        SQLiteDatabase db;
        try {
            db = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            db = dbHelper.getReadableDatabase();
        }
    }
}
